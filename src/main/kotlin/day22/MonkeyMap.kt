package day22

import util.*
import util.Helpers.Companion.toGrid

enum class POI {
    EMPTY, OPEN, WALL
}

sealed interface Direction
enum class Facing(val vector: Point) : Direction {
    LEFT(Point(-1, 0)),
    RIGHT(Point(1, 0)),
    UP(Point(0, -1)),
    DOWN(Point(0, 1));

    fun turnLeft(): Facing = when (this) {
        LEFT -> DOWN
        RIGHT -> UP
        UP -> LEFT
        DOWN -> RIGHT
    }

    fun turnRight(): Facing = when (this) {
        LEFT -> UP
        RIGHT -> DOWN
        UP -> RIGHT
        DOWN -> LEFT
    }

    fun isHorizontal(): Boolean = this == LEFT || this == RIGHT
}

data class Steps(val steps: Int) : Direction

class MonkeyMap(fileName: String) : Solution<Either<List<POI>, List<Direction>>, Long>(fileName) {
    override fun parse(line: String): Either<List<POI>, List<Direction>>? {
        return when {
            (line.any { it.isDigit() }) -> {
                data class FoldState(val numberInProgress: String? = null, val acc: List<Direction> = emptyList())

                val folded = line.fold(FoldState()) { s, c ->
                    when {
                        c.isDigit() -> s.copy(numberInProgress = (s.numberInProgress ?: "") + c)
                        else -> {
                            val stepsList =
                                if (s.numberInProgress == null) emptyList() else listOf(Steps(s.numberInProgress.toInt()))
                            val facing = if (c == 'L') Facing.LEFT else Facing.RIGHT
                            s.copy(acc = s.acc + stepsList + facing, numberInProgress = null)
                        }
                    }
                }


                Right(folded.acc + Steps(folded.numberInProgress!!.toInt()))

            }

            line.isEmpty() -> null
            else -> Left(line.map {
                when (it) {
                    ' ' -> POI.EMPTY
                    '.' -> POI.OPEN
                    '#' -> POI.WALL
                    else -> TODO("Unknown character in map: $it")
                }
            })
        }

    }

    override fun solve1(data: List<Either<List<POI>, List<Direction>>>): Long {
        val map = data.mapNotNull { it.left }
            .toGrid()
            .mapKeys { Point(it.key.x + 1, it.key.y + 1) }
            .filterValues { it != POI.EMPTY }

        fun wrap(p: Point, isHorizontal: Boolean): Point {
            return if (isHorizontal) {
                val row = map.keys.filter { it.y == p.y }.map { it.x }
                val minX = row.min()
                val maxX = row.max()
                when {
                    (p.x < minX) -> Point(maxX, p.y)
                    (p.x > maxX) -> Point(minX, p.y)
                    else -> p
                }
            } else {
                val row = map.keys.filter { it.x == p.x }.map { it.y }
                val minY = row.min()
                val maxY = row.max()
                when {
                    (p.y < minY) -> Point(p.x, maxY)
                    (p.y > maxY) -> Point(p.x, minY)
                    else -> p
                }
            }
        }

        tailrec fun doSteps(position: Point, facing: Facing, steps: Int): Point {

            return if (steps == 0) position else {
                val nextPos = wrap(position + facing.vector, facing.isHorizontal())
                val next = when (map[nextPos]) {
                    null -> TODO("Wrap around $position, facing: $facing")
                    POI.WALL -> position
                    POI.OPEN -> nextPos
                    else -> TODO()
                }

                doSteps(next, facing, steps - 1)
            }
        }

        val steps = data.firstNotNullOf { it.right }

        val start = map.entries.first { it.value == POI.OPEN }.key

        data class Traveller(val position: Point = start, val facing: Facing = Facing.RIGHT)

        val finalState = steps.fold(Traveller()) { traveller, step ->
            when (step) {
                is Steps -> {
                    val newPosition = doSteps(traveller.position, traveller.facing, step.steps)
                    traveller.copy(position = newPosition)
                }

                Facing.LEFT -> traveller.copy(facing = traveller.facing.turnLeft())
                Facing.RIGHT -> traveller.copy(facing = traveller.facing.turnRight())
                else -> TODO("Not an instruction, ")
            }
        }
        return finalState.position.y * 1000 + finalState.position.x * 4 + when (finalState.facing) {
            Facing.RIGHT -> 0
            Facing.DOWN -> 1
            Facing.LEFT -> 2
            Facing.UP -> 3
        }
    }

    override fun solve2(data: List<Either<List<POI>, List<Direction>>>): Long {
        TODO("Not yet implemented")
    }
}