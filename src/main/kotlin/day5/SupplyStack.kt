package day5

import util.Helpers.Companion.pad
import util.Helpers.Companion.transpose
import util.Solution

sealed interface StackInput
object EmptyLine : StackInput
data class StackContainers(val stacks: List<Char?>) : StackInput
data class Move(val amount: Int, val from: Int, val to: Int) : StackInput

val moveRegex = """move (\d+) from (\d) to (\d)""".toRegex()

class SupplyStack(fileName: String) : Solution<StackInput, String>(fileName) {

    override fun parse(line: String): StackInput? {
        return when {
            line.isBlank() -> EmptyLine
            line.trimStart().first() == '1' -> null
            line.startsWith("move") -> {
                moveRegex.matchEntire(line)?.let {
                    Move(it.groupValues[1].toInt(), it.groupValues[2].toInt() - 1, it.groupValues[3].toInt() - 1)
                }
            }

            else -> {
                val parts = line.chunked(4)
                parts.map {
                    it[1].let { c ->
                        if (c == ' ') null else c
                    }
                }.let {
                    StackContainers(it)
                }
            }
        }
    }

    override fun List<StackInput>.solve1(): String {
        return moveCrates(is9001 = false)
    }

    override fun List<StackInput>.solve2(): String {
        return moveCrates(is9001 = true)
    }

    private fun List<StackInput>.moveCrates(is9001: Boolean): String {
        val stackLayers: List<List<Char?>> = this.filterIsInstance<StackContainers>().map { it.stacks }
        val stacks = stackLayers
            .map { it.pad(stackLayers.maxOf { l -> l.size }) }
            .transpose().map { it.filterNotNull().reversed() }
        val moves = this.filterIsInstance<Move>()
        val finalStacks = moves.fold(stacks) { s, move ->
            val stacksToMove = s[move.from].takeLast(move.amount)
            val moving = if (is9001) stacksToMove else stacksToMove.reversed()
            s.mapIndexed { index, chars ->
                when (index) {
                    move.from -> chars.dropLast(move.amount)
                    move.to -> chars + moving
                    else -> chars
                }
            }
        }

        return finalStacks
            .filter { it.isNotEmpty() }
            .map { it.last() }
            .joinToString("")
    }
}
