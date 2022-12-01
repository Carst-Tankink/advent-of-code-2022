package day1

import util.Solution

class CalorieCounting(fileName: String) : Solution<Int, Int>(fileName) {
    override fun parse(line: String): Int? = if (line.isBlank()) null else line.toInt()

    override fun List<Int?>.solve1(): Int {
        val bags: List<List<Int>> = accumulateToGroups(this)

        return bags.maxOfOrNull { it.sum() } ?: -1
    }

    override fun List<Int?>.solve2(): Int {
        val bags: List<List<Int>> = accumulateToGroups(this)

        return bags.map { it.sum() }.sortedDescending().take(3).sum()
    }

    tailrec fun accumulateToGroups(
        remaining: List<Int?>,
        current: List<Int> = emptyList(),
        acc: List<List<Int>> = emptyList()
    ): List<List<Int>> {
        return if (remaining.isEmpty()) acc + listOf(current) else {
            val next = remaining.first()
            val tail = remaining.drop(1)
            if (next == null) {
                accumulateToGroups(tail, emptyList(), acc + listOf(current))
            } else {
                accumulateToGroups(tail, current + next, acc)
            }
        }
    }
}
