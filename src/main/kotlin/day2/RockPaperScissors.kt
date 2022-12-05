package day2

import day2.RockPaperScissors.Move.*
import util.Solution

class RockPaperScissors(fileName: String) : Solution<Pair<RockPaperScissors.Move, String>, Int>(fileName) {
    enum class Move {
        ROCK,
        PAPER,
        SCISSORS
    }

    private val moveScores: Map<Move, Int> = mapOf(
        ROCK to 1,
        PAPER to 2,
        SCISSORS to 3
    )

    private val moveWins: Map<Move, Move> = mapOf(
        ROCK to SCISSORS,
        PAPER to ROCK,
        SCISSORS to PAPER
    )

    private val moveLoses = moveWins.entries.associateBy({ it.value }) { it.key }

    override fun parse(line: String): Pair<Move, String> {
        val parts = line.split(' ')

        return Pair(elfMove(parts[0]), parts[1])
    }

    override fun List<Pair<Move, String>>.solve1(): Int {
        return this
            .sumOf { (elf, ours) -> scoreMove(elf, ourMove(ours)) }
    }

    private fun scoreMove(elfMove: Move, ourMove: Move): Int {
        val winScore = when {
            ourMove == elfMove -> 3
            moveWins[ourMove] == elfMove -> 6
            else -> 0
        }
        return (moveScores[ourMove] ?: -1) + winScore
    }

    override fun List<Pair<Move, String>>.solve2(): Int {
        return this
            .map { (elfMove, strategy) -> Pair(elfMove, pickMove(elfMove, strategy)) }
            .sumOf { (elfMove, ourMove) -> scoreMove(elfMove, ourMove) }
    }

    private fun pickMove(elfMove: Move, strategy: String): Move {
        return when (strategy) {
            "X" -> moveWins[elfMove]!!
            "Y" -> elfMove
            "Z" -> moveLoses[elfMove]!!
            else -> TODO("Unknown strategy")
        }
    }

    private fun elfMove(code: String): Move {
        return when (code) {
            "A" -> ROCK
            "B" -> PAPER
            "C" -> SCISSORS
            else -> TODO("No lookup for $code")
        }
    }

    private fun ourMove(code: String): Move {
        return when (code) {
            "X" -> ROCK
            "Y" -> PAPER
            "Z" -> SCISSORS
            else -> TODO("No lookup for $code")
        }
    }
}
