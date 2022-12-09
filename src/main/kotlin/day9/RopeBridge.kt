package day9

import util.Point
import util.Solution

enum class Direction {
    LEFT, RIGHT, UP, DOWN
}

data class RopeMove(val dir: Direction, val steps: Int)
class RopeBridge(fileName: String) : Solution<RopeMove, Int>(fileName) {
    override fun parse(line: String): RopeMove {
        val steps = line.drop(2).toInt()
        return when (line.first()) {
            'L' -> RopeMove(Direction.LEFT, steps)
            'R' -> RopeMove(Direction.RIGHT, steps)
            'U' -> RopeMove(Direction.UP, steps)
            'D' -> RopeMove(Direction.DOWN, steps)
            else -> TODO("Unknown directive $line")
        }
    }

    override fun solve1(data: List<RopeMove>): Int {
        data class FoldState(val head: Point = Point(0, 0), val tailPositions: List<Point> = listOf(Point(0, 0)))

        tailrec fun doMove(state: FoldState, dirVec: Point, steps: Int): FoldState {
            return if (steps == 0) state else {
                val tail = state.tailPositions.first()
                val head = state.head + dirVec
                val newTail = if (tail == head || tail in head.getNeighbours(false)) {
                    tail
                } else {
                    val diff = head - tail

                    val x: Long = when {
                        diff.x < 0 -> -1
                        diff.x == 0L -> 0
                        else -> 1
                    }
                    val y: Long = when {
                        diff.y < 0 -> -1
                        diff.y == 0L -> 0
                        else -> 1
                    }
                    
                    Point(tail.x + x, tail.y + y)
                }
                doMove(state.copy(head = head, tailPositions = listOf(newTail) + state.tailPositions), dirVec, steps - 1)
            }

        }

        val finalState = data.fold(FoldState()) { state, move ->
            val dirVec = when (move.dir) {
                Direction.LEFT -> Point(-1, 0)
                Direction.RIGHT -> Point(1, 0)
                Direction.UP -> Point(0, 1)
                Direction.DOWN -> Point(0, -1)
            }
            doMove(state, dirVec, move.steps)
        }

        return finalState.tailPositions.toSet().size
    }

    override fun solve2(data: List<RopeMove>): Int {
        TODO("Not yet implemented")
    }
}