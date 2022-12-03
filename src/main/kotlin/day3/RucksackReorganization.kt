package day3

import util.Solution

class RucksackReorganization(fileName: String) : Solution<Pair<List<Char>, List<Char>>, Int>(fileName) {
    override fun parse(line: String): Pair<List<Char>, List<Char>> {
        return line.chunked(line.length / 2).let {
            Pair(it[0].toList(), it[1].toList())
        }
    }

    override fun List<Pair<List<Char>, List<Char>>?>.solve1(): Int {
        return this.filterNotNull()
            .map { (compartment1, compartment2) -> compartment1.intersect(compartment2.toSet()).first() }
            .sumOf { priority(it) }
    }

    override fun List<Pair<List<Char>, List<Char>>?>.solve2(): Int {
        return this.asSequence()
            .filterNotNull()
            .map { (it.first + it.second).toSet() }
            .chunked(3)
            .map { it[0].intersect(it[1]).intersect(it[2]).first() }
            .sumOf { priority(it) }
    }

    private fun priority(it: Char) = it.code - (if (it.isLowerCase()) 96 else 38)
}
