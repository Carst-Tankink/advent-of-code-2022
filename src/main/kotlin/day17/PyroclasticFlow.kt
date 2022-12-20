package day17

import day17.Rock.*
import util.Point
import util.Solution

enum class Rock(val spots: Set<Point>) {
    HORIZONTAL(setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0))),
    CROSS(setOf(Point(0, 1), Point(1, 0), Point(1, 1), Point(1, 2), Point(2, 1))),
    CORNER(setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(2, 1), Point(2, 2))),
    VERTICAL(setOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3))),
    SQUARE(setOf(Point(0, 0), Point(0, 1), Point(1, 0), Point(1, 1)))
}

val rockOrder: List<Rock> = listOf(HORIZONTAL, CROSS, CORNER, VERTICAL, SQUARE)

enum class Dir {
    LEFT, RIGHT
}

class PyroclasticFlow(fileName: String) : Solution<List<Dir>, Long>(fileName) {
    override fun parse(line: String): List<Dir> = line.map { if (it == '<') Dir.LEFT else Dir.RIGHT }

    override fun solve1(data: List<List<Dir>>): Long {
        tailrec fun dropRocks(
            dropped: Int = 0,
            moves: List<Dir> = data.first(),
            rocks: List<Rock> = rockOrder,
            grid: Set<Point> = emptySet()
        ): Set<Point> {
            tailrec fun dropRock(rockPoints: Set<Point>, moves: List<Dir>): Pair<Set<Point>, List<Dir>> {
                val nextMove = moves.first()
                val movesDone = moves.drop(1) + nextMove
                val move = if (nextMove == Dir.LEFT) Point(-1, 0) else Point(1, 0)
                val moved = rockPoints.map { it + move }
                val afterMove =
                    if (moved.all { it.x in 0 until 7 } && moved.none { it in grid }) moved else rockPoints
                val droppedRock = afterMove.map { it + Point(0, -1) }.toSet()
                return if (droppedRock.any { it in grid || it.y < 0L }) Pair(grid + afterMove, movesDone)
                else dropRock(droppedRock, movesDone)
            }

            return if (dropped == 2022) grid else {
                val maxHeight = grid.maxOfOrNull { it.y + 1 } ?: 0
                val rockLine = rocks.first().spots.map { it + Point(2, maxHeight + 3) }.toSet()
                val (newHeights, newMoves) = dropRock(rockLine, moves)
                dropRocks(dropped + 1, newMoves, rocks.drop(1) + rocks.first(), newHeights)
            }
        }

        return dropRocks().maxOf { it.y } + 1
    }

    override fun solve2(data: List<List<Dir>>): Long {
        TODO("Not yet implemented")
    }
}