package day4

import util.Solution

data class Range(val start: Int, val end: Int) {
    fun isWithin(other: Range): Boolean =
        other.start <= this.start && this.end <= other.end

    fun overlaps(other: Range): Boolean = other.start <= this.start && this.start <= other.end
}

class CampCleanup(fileName: String) : Solution<Pair<Range, Range>, Int>(fileName) {
    override fun parse(line: String): Pair<Range, Range> {
        return line.split(',')
            .map { r ->
                r.split('-').let {
                    Range(it[0].toInt(), it[1].toInt())
                }
            }.let {
                Pair(it[0], it[1])
            }
    }

    override fun List<Pair<Range, Range>>.solve1(): Int {
        return this
            .count { (r1, r2) -> r1.isWithin(r2) || r2.isWithin(r1) }
    }

    override fun List<Pair<Range, Range>>.solve2(): Int {
        return this
            .count { (r1, r2) -> r1.overlaps(r2) || r2.overlaps(r1) }
    }
}
