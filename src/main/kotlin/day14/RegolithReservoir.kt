package day14

import util.Point
import util.Solution
import kotlin.math.max
import kotlin.math.min

class RegolithReservoir(fileName: String) : Solution<Set<Point>, Int>(fileName) {
    override fun parse(line: String): Set<Point>? {
        val coordinates = line.split(" -> ")
            .map {
                it.split(',').let { parts ->
                    Point(parts[0].toLong(), parts[1].toLong())
                }
            }

        data class FoldState(val pointsCovered: Set<Point> = emptySet(), val previousCoordinate: Point? = null)

        val blockedByPath = coordinates.fold(FoldState()) { s, p ->
            val prev = s.previousCoordinate
            val newSet = if (prev == null) s.pointsCovered else {
                val path = if (p.x == prev.x) {
                    (min(p.y, prev.y)..max(p.y, prev.y)).map { Point(p.x, it) }.toSet()
                } else {
                    (min(p.x, prev.x)..max(p.x, prev.x)).map { Point(it, p.y) }.toSet()
                }
                s.pointsCovered + path
            }

            FoldState(newSet, p)
        }

        return blockedByPath.pointsCovered

    }

    override fun solve1(data: List<Set<Point>>): Int {
        val allPaths = data.flatten().toSet()
        val maxY = allPaths.maxOf { it.y }
        return simulateSand(allPaths) { p -> p.y > maxY}
    }

    override fun solve2(data: List<Set<Point>>): Int {
        val allPaths = data.flatten().toSet()
        return simulateSand(allPaths) { false } + 1 // Off by one because stop condition is based on next position, not current
    }

    private fun simulateSand(allPaths: Set<Point>, stop: (Point) -> Boolean): Int {
        val maxY = allPaths.maxOf { it.y }
        val moves = listOf(Point(0, 1), Point(-1, 1), Point(1, 1))
        tailrec fun dropGrain(blockedPoints: Set<Point>, position: Point = Point(500, 0)): Point? {
            val nextPoint = moves
                .map { position + it }
                .find { it !in blockedPoints && it.y < maxY + 2 }

            return when {
                nextPoint == null && position == Point(500,0) -> null // Part 2 end
                nextPoint == null -> position // Rest
                stop(nextPoint) -> null // Out of bounds
                else -> dropGrain(blockedPoints, nextPoint) // Can drop down
            }
        }

        tailrec fun dropSand(sand: Set<Point> = emptySet()): Set<Point> {
            val droppedGrain: Point? = dropGrain(allPaths.union(sand))
            return if (droppedGrain == null) sand else {
                val caveSand = sand + droppedGrain
                dropSand(caveSand)
            }
        }

        return dropSand().size
    }

}