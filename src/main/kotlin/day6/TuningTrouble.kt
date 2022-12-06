package day6

import util.Solution

class TuningTrouble(fileName: String) : Solution<String, Int>(fileName) {
    override fun parse(line: String): String = line

    override fun solve1(data: List<String>): Int {
        return data.findStart(4)
    }

    override fun solve2(data: List<String>): Int {
        return data.findStart(14)
    }

    private fun List<String>.findStart(windowSize: Int): Int {
        return this.first()
            .windowed(windowSize)
            .withIndex()
            .first { it.value.toSet().count() == windowSize }.index + windowSize
    }
}