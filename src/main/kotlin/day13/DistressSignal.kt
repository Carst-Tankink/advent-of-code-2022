package day13

import util.Solution

sealed interface Packet
data class NestedList(val contents: List<Packet>) : Packet
data class Number(val number: Int) : Packet
object Separator : Packet

class DistressSignal(fileName: String) : Solution<Packet, Int>(fileName) {
    fun parseLine(l: String, children: List<Packet> = emptyList()): Pair<Packet, String>? {
        return if (l.isEmpty()) Pair(NestedList(children), l) else {
            val head = l.first()
            when {
                head.isDigit() -> {
                    val number = l.takeWhile { it.isDigit() }.toInt()
                    parseLine(l.dropWhile { it.isDigit() }, children + Number(number))
                }

                head == ',' -> parseLine(l.drop(1), children)
                head == '[' -> {
                    val (subTree, remaining) = parseLine(l.drop(1), emptyList())!!
                    parseLine(remaining, children + subTree)
                }

                head == ']' -> Pair(NestedList(children), l.drop(1))
                else -> {
                    println("Unmatched token $head")
                    null
                }
            }
        }

    }

    override fun parse(line: String): Packet? = if (line.isBlank()) Separator else {
        parseLine(line)?.first
    }

    override fun solve1(data: List<Packet>): Int {
        data class FoldPairs(
            val left: Packet? = null,
            val right: Packet? = null,
            val acc: List<Pair<Packet, Packet>> = emptyList()
        )

        val pairs: List<Pair<Packet, Packet>> = data.fold(FoldPairs()) { state, p ->
            when (p) {
                is Separator -> FoldPairs(acc = state.acc + Pair(state.left!!, state.right!!))
                else -> if (state.left == null) FoldPairs(left = p, right = null, acc = state.acc) else {
                    FoldPairs(left = state.left, right = p, acc = state.acc)
                }
            }

        }.acc

        return pairs.withIndex().filter { (_, value) -> inRightOrder(value.first, value.second)!! }
            .sumOf { it.index + 1 }

    }

    private fun inRightOrder(left: Packet, right: Packet): Boolean? {
        return when {
            left is Number && right is Number -> if (left == right) null else {
                left.number < right.number
            }

            left is NestedList && right is NestedList -> listsInRightOrder(left.contents, right.contents)
            left is NestedList && right is Number -> listsInRightOrder(left.contents, listOf(right))
            left is Number && right is NestedList -> listsInRightOrder(listOf(left), right.contents)
            else -> TODO()
        }
    }

    private fun listsInRightOrder(left: List<Packet>, right: List<Packet>): Boolean? {
        return when {
            (left.isEmpty() && right.isEmpty()) -> null
            (left.isEmpty()) -> true
            (right.isEmpty()) -> false
            else -> inRightOrder(left.first(), right.first()) ?: listsInRightOrder(left.drop(1), right.drop(1))
        }
    }

    override fun solve2(data: List<Packet>): Int {
        val divider1 = NestedList(listOf(NestedList(listOf(Number(2)))))
        val divider2 = NestedList(listOf(NestedList(listOf(Number(6)))))
        val fullData = data.filterNot {it is Separator} + divider1 + divider2

        val solution = fullData.sortedWith { p1, p2 ->
            val result = inRightOrder(p1, p2)
            if (result == null) 0 else if (result) -1 else 1
        }

        return (solution.indexOf(divider1) + 1) * (solution.indexOf(divider2) + 1)
    }
}