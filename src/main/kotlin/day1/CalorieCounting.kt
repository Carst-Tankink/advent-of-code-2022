package day1

import util.Solution
import util.accumulateToGroups

class CalorieCounting(fileName: String) : Solution<Int, Int>(fileName) {
    override fun parse(line: String): Int? = if (line.isBlank()) null else line.toInt()

    override fun List<Int?>.solve1(): Int {
        return accumulateToGroups().topN(1)
    }

    override fun List<Int?>.solve2(): Int {
        return accumulateToGroups().topN(3)
    }

    private fun List<List<Int>>.topN(n: Int) =
        this.map { it.sum() }
            .sortedDescending()
            .take(n)
            .sum()
}
