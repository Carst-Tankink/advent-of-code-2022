package day9

import util.Point
import util.Solution
import kotlin.math.sign

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
        val initialRope = List(2) { Point(0, 0) }
        return pullRope(initialRope, data)
    }

    private fun pullRope(
        initialRope: List<Point>,
        data: List<RopeMove>
    ): Int {
        data class FoldState(
            val rope: List<Point> = initialRope,
            val tailPositions: Set<Point> = setOf(initialRope.last())
        )

        fun moveRope(rope: List<Point>, dirVec: Point): List<Point> {
            val newHead = rope.first() + dirVec
            return rope.drop(1).fold(listOf(newHead)) { newPositions, knot ->
                val previousKnot = newPositions.last()
                val previousKnotNeighbours: List<Point> = previousKnot.let {
                    listOf(it) + it.getNeighbours(cardinal = false)
                }

                val newKnot = if (knot in previousKnotNeighbours) knot else {
                    val (diffX, diffY) = previousKnot - knot

                    Point(knot.x + diffX.sign, knot.y + diffY.sign)
                }

                newPositions + newKnot

            }
        }

        tailrec fun doMove(state: FoldState, dirVec: Point, steps: Int): FoldState {
            return if (steps == 0) state else {
                val newRope = moveRope(state.rope, dirVec)
                doMove(FoldState(newRope, state.tailPositions + newRope.last()), dirVec, steps - 1)
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
        return pullRope(List(10) { Point(0, 0) }, data)
    }
}