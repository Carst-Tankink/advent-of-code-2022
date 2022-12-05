package day1

import util.Solution
import util.accumulateToGroups

sealed interface CalorieInput
object EmptyCalories : CalorieInput
data class Calories(val value: Int) : CalorieInput

class CalorieCounting(fileName: String) : Solution<CalorieInput, Int>(fileName) {
    override fun parse(line: String): CalorieInput = if (line.isBlank()) EmptyCalories else Calories(line.toInt())

    override fun List<CalorieInput>.solve1(): Int {
        return this
            .map { if (it is Calories) it.value else null }
            .accumulateToGroups().topN(1)
    }

    override fun List<CalorieInput>.solve2(): Int {
        return this
            .map { if (it is Calories) it.value else null }
            .accumulateToGroups().topN(3)
    }

    private fun List<List<Int>>.topN(n: Int) =
        this.map { it.sum() }
            .sortedDescending()
            .take(n)
            .sum()
}
