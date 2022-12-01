package day1

import util.Solution
import util.accumulateToGroups

class CalorieCounting(fileName: String) : Solution<Int, Int>(fileName) {
    override fun parse(line: String): Int? = if (line.isBlank()) null else line.toInt()

    override fun List<Int?>.solve1(): Int {
        val bags: List<List<Int>> = accumulateToGroups()
        return bags.maxOfOrNull { it.sum() } ?: -1
    }

    override fun List<Int?>.solve2(): Int {
        val bags: List<List<Int>> = accumulateToGroups()

        return bags.map { it.sum() }.sortedDescending().take(3).sum()
    }
}
