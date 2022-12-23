package day23

import day23.Orthogonal.*
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

enum class MapLocation {
    ELF, EMPTY
}

enum class Orthogonal(val toConsider: List<Point>, val vector: Point) {
    NORTH(listOf(Point(0, -1), Point(1, -1), Point(-1, -1)), Point(0, -1)),
    SOUTH(listOf(Point(0, 1), Point(1, 1), Point(-1, 1)), Point(0, 1)),
    WEST(listOf(Point(-1, 0), Point(-1, 1), Point(-1, -1)), Point(-1, 0)),
    EAST(listOf(Point(1, 0), Point(1, 1), Point(1, -1)), Point(1, 0))
}

class UnstableDiffusion(fileName: String) : Solution<List<MapLocation>, Long>(fileName) {
    override fun parse(line: String): List<MapLocation> =
        line.map { if (it == '.') MapLocation.EMPTY else MapLocation.ELF }

    override fun solve1(data: List<List<MapLocation>>): Long {
        tailrec fun spreadOut(
            elfPositions: Set<Point>,
            order: List<Orthogonal>,
            rounds: Int = 0
        ): Set<Point> {
            fun findFreePosition(position: Point): Point {
                val possibleDir =
                    order.firstOrNull { dir ->
                        dir.toConsider.map { position + it }.none { it in elfPositions }
                    }?.vector

                return position + (possibleDir ?: Point(0, 0))
            }

            val freeElves = elfPositions.filter { p ->
                p.getNeighbours(false).none { it in elfPositions }
            }.toSet()
            return if (rounds == 10 || freeElves.size == elfPositions.size) elfPositions else {
                val proposedPositions: Map<Point, Point> =
                    elfPositions.associateWith { if (it in freeElves) it else findFreePosition(it) }
                val positionsCounts: Map<Point, Int> = proposedPositions.values.groupingBy { it }.eachCount()
                val newPositions =
                    proposedPositions.map { if (positionsCounts[it.value]!! == 1) it.value else it.key }.toSet()
                spreadOut(newPositions, order.drop(1) + order.first(), rounds + 1)
            }
        }

        val pointsWithElves = data.toGrid().filterValues { it == MapLocation.ELF }.keys.map { it + Point(1, 1) }.toSet()

        val finalState = spreadOut(pointsWithElves, listOf(NORTH, SOUTH, WEST, EAST))
        val horizontalSide = finalState.maxOf { it.x } - finalState.minOf { it.x } + 1
        val verticalSide = finalState.maxOf { it.y } - finalState.minOf { it.y } + 1

        return (horizontalSide * verticalSide) - finalState.size
    }


    override fun solve2(data: List<List<MapLocation>>): Long {
        tailrec fun spreadOut(
            elfPositions: Set<Point>,
            order: List<Orthogonal>,
            rounds: Long = 0
        ): Long {
            fun findFreePosition(position: Point): Point {
                val possibleDir =
                    order.firstOrNull { dir ->
                        dir.toConsider.map { position + it }.none { it in elfPositions }
                    }?.vector

                return position + (possibleDir ?: Point(0, 0))
            }

            val freeElves = elfPositions.filter { p ->
                p.getNeighbours(false).none { it in elfPositions }
            }.toSet()
            return if (freeElves.size == elfPositions.size) rounds else {
                val proposedPositions: Map<Point, Point> =
                    elfPositions.associateWith { if (it in freeElves) it else findFreePosition(it) }
                val positionsCounts: Map<Point, Int> = proposedPositions.values.groupingBy { it }.eachCount()
                val newPositions =
                    proposedPositions.map { if (positionsCounts[it.value]!! == 1) it.value else it.key }.toSet()
                spreadOut(newPositions, order.drop(1) + order.first(), rounds + 1)
            }
        }

        val pointsWithElves = data.toGrid().filterValues { it == MapLocation.ELF }.keys.map { it + Point(1, 1) }.toSet()

        return spreadOut(pointsWithElves, listOf(NORTH, SOUTH, WEST, EAST))
    }
}
    