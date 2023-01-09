package day22

import util.*
import util.Helpers.Companion.toGrid

enum class POI {
    EMPTY, OPEN, WALL
}

sealed interface Direction

data class Steps(val steps: Int) : Direction
data class Turn(val facing: Facing) : Direction

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
                            s.copy(acc = s.acc + stepsList + Turn(facing), numberInProgress = null)
                        }
                    }
                }

                Right(folded.acc + Steps(folded.numberInProgress!!.toInt()))
            }

            line.isEmpty() -> null
            else -> Left(
                line.map {
                    when (it) {
                        ' ' -> POI.EMPTY
                        '.' -> POI.OPEN
                        '#' -> POI.WALL
                        else -> TODO("Unknown character in map: $it")
                    }
                }
            )
        }
    }

    override fun solve1(data: List<Either<List<POI>, List<Direction>>>): Long {
        val (map, steps) = prepareData(data)

        return walkMap(map, steps) { p, facing ->
            val point = if (facing.isHorizontal()) {
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

            Pair(point, facing)
        }
    }

    private fun prepareData(data: List<Either<List<POI>, List<Direction>>>): Pair<Map<Point, POI>, List<Direction>> {
        return Pair(
            data.mapNotNull { it.left }
                .toGrid()
                .mapKeys { Point(it.key.x + 1, it.key.y + 1) }
                .filterValues { it != POI.EMPTY },
            data.firstNotNullOf { it.right }
        )
    }

    private fun walkMap(
        map: Map<Point, POI>,
        steps: List<Direction>,
        wrap: (Point, Facing) -> Pair<Point, Facing>
    ): Long {
        tailrec fun doSteps(position: Point, facing: Facing, steps: Int): Point {
            return if (steps == 0) position else {
                val (nP, nF) = wrap(position + facing.vector, facing)
                val (nextPosition, nextFacing) = when (map[nP]) {
                    null -> TODO("Wrap around $position, facing: $facing")
                    POI.WALL -> Pair(position, facing)
                    POI.OPEN -> Pair(nP, nF)
                    else -> TODO("Empty is not part of map")
                }

                doSteps(nextPosition, nextFacing, steps - 1)
            }
        }

        val start = map.entries.first { it.value == POI.OPEN }.key

        data class Traveller(val position: Point = start, val facing: Facing = Facing.RIGHT)

        val finalState = steps.fold(Traveller()) { traveller, step ->
            when (step) {
                is Steps -> {
                    val newPosition = doSteps(traveller.position, traveller.facing, step.steps)
                    traveller.copy(position = newPosition)
                }

                Turn(Facing.LEFT) -> traveller.copy(facing = traveller.facing.turnLeft())
                Turn(Facing.RIGHT) -> traveller.copy(facing = traveller.facing.turnRight())
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
        val (map, steps) = prepareData(data)

        // Offsets of each face in the map. Offset is the top-left coordinate of each face
        val faces: Map<String, Point> = if (isSample) mapOf(
            "TOP" to Point(8, 0),
            "BACK" to Point(0, 4),
            "LEFT" to Point(4, 4),
            "FRONT" to Point(8, 4),
            "BOTTOM" to Point(8, 8),
            "RIGHT" to Point(12, 8)
        ) else mapOf(
            "TOP" to Point(50, 0),
            "BACK" to Point(0, 150),
            "LEFT" to Point(0, 100),
            "FRONT" to Point(50, 50),
            "BOTTOM" to Point(50, 100),
            "RIGHT" to Point(100, 0)
        )

        fun translateFacings(p: Point, f: Facing): Facing {
            TODO()
        }

        fun translatePoint(p: Point): Point {
            TODO()
        }

        return walkMap(map, steps) { p, f ->
            TODO("Not yet implemented")
        }
    }
}
