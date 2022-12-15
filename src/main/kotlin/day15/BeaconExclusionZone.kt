package day15

import util.Point
import util.Solution

data class Sensor(val pos: Point, val beaconPos: Point) {
    val beaconDistance = pos.manhattanDistance(beaconPos)
    fun covers(p: Point) = pos.manhattanDistance(p) <= beaconDistance
}

val SENSOR_REGEX = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()

class BeaconExclusionZone(fileName: String) : Solution<Sensor, Long>(fileName) {
    override fun parse(line: String): Sensor? = SENSOR_REGEX
        .matchEntire(line)
        ?.groupValues
        ?.let { values ->
            Sensor(Point(values[1].toLong(), values[2].toLong()), Point(values[3].toLong(), values[4].toLong()))
        }

    override fun solve1(data: List<Sensor>): Long {
        val beaconPositions = data.map { it.beaconPos }.toSet()
        val smallestX = data.minOf { it.pos.x - it.beaconDistance }
        val largestX = data.maxOf { it.pos.x + it.beaconDistance }

        val coveredAt = (smallestX..largestX).map {
            Point(it, if (isSample) 10 else 2_000_000)
        }.filter { p -> data.any { it.covers(p) } }.toSet()

        val noBeacons = coveredAt - beaconPositions

        return noBeacons.size.toLong()
    }

    override fun solve2(data: List<Sensor>): Long {
        val distress = data
            .flatMap {
                val maxDistance = it.beaconDistance + 1
                (-maxDistance..maxDistance).map { d -> it.pos + Point(d, maxDistance - d) }
            }
            .filter { (x, y) ->
                val maxRange = if (isSample) 20 else 4_000_000
                x in 0..maxRange && y in 0..maxRange
            }
            .find { p -> data.none { it.covers(p) } }!!
        return distress.x * 4_000_000 + distress.y
    }
}