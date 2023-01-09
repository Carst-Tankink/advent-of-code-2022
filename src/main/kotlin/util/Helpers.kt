@file:Suppress("unused")

package util

import kotlin.math.abs

sealed class Either<L, R>(val left: L?, val right: R?)
class Left<L, R>(value: L) : Either<L, R>(left = value, right = null)
class Right<L, R>(value: R) : Either<L, R>(left = null, right = value)

data class Point(val x: Long, val y: Long) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    operator fun minus(other: Point): Point {
        return Point(x - other.x, y - other.y)
    }

    fun getAllNeighbours(): List<Point> {
        val allPoints = listOf(
            Point(-1, -1), Point(0, -1), Point(1, -1),
            Point(-1, 0), Point(0, 0), Point(1, 0),
            Point(-1, 1), Point(0, 1), Point(1, 1)
        )

        return allPoints.map { this + it }
    }

    fun getNeighbours(cardinal: Boolean): List<Point> {
        val nonCardinal = if (cardinal) emptyList() else listOf(
            Point(-1, -1), Point(1, -1),
            Point(-1, 1), Point(1, 1)
        )
        return (
            nonCardinal + listOf(
                Point(0, -1),
                Point(1, 0),
                Point(0, 1),
                Point(-1, 0)
            )
            ).map { dir -> this + dir }
    }

    fun manhattanDistance(p2: Point): Long = abs(this.x - p2.x) + abs(this.y - p2.y)
}

data class Point3D(val x: Long, val y: Long, val z: Long) {
    operator fun plus(other: Point3D): Point3D = this.copy(x + other.x, y + other.y, z + other.z)
    fun neighbours(): List<Point3D> {
        return listOf(
            Point3D(1, 0, 0),
            Point3D(-1, 0, 0),
            Point3D(0, 1, 0),
            Point3D(0, -1, 0),
            Point3D(0, 0, 1),
            Point3D(0, 0, -1)
        ).map { this + it }
    }
}

typealias Grid<T> = Map<Point, T>

fun <T> Grid<T>.width(): Long {
    return this.maxOf { it.key.x } + 1
}

fun <T> Grid<T>.height(): Long {
    return this.maxOf { it.key.y } + 1
}

class Helpers {
    companion object {
        fun Char.intValue() = this.code - 48

        fun <E> List<E>.pad(height: Int, e: E? = null): List<E?> {
            return if (this.size == height) this else {
                (this + e).pad(height)
            }
        }

        fun <T> List<List<T>>.transpose(): List<List<T>> {
            return if (this.any { it.isEmpty() }) emptyList() else {
                listOf(this.map { it[0] }) + this.map { it.drop(1) }.transpose()
            }
        }

        fun toDecimal(digits: List<Int>, base: Int): Long {
            tailrec fun rec(acc: Long, power: Long, remaining: List<Int>): Long {
                return if (remaining.isEmpty()) acc else {
                    rec(acc + power * remaining[0], power * base, remaining.drop(1))
                }
            }

            return rec(0, 1, digits.reversed())
        }

        fun <T> List<List<T>>.toGrid(): Grid<T> {
            return this.mapIndexed { y, line ->
                line.mapIndexed { x, t ->
                    Point(x.toLong(), y.toLong()) to t
                }
            }
                .flatten()
                .toMap()
        }

        fun <T> printGrid(g: Grid<T>): String {
            val minY = g.minOf { it.key.y }
            val maxY = g.maxOf { it.key.y }

            val minX = g.minOf { it.key.x }
            val maxX = g.maxOf { it.key.x }

            return (minY..maxY).joinToString("\n") { y ->
                (minX..maxX).joinToString("") { x ->
                    g[Point(x, y)].toString()
                }
            }
        }

        fun printGrid(filledPoints: Set<Point>): String {
            val minY = filledPoints.minOf { it.y }
            val maxY = filledPoints.maxOf { it.y }
            val verticalBorder = minY..maxY

            val minX = filledPoints.minOf { it.x }
            val maxX = filledPoints.maxOf { it.x }
            return verticalBorder.joinToString("\n") { y ->
                (minX..maxX).joinToString("") { x ->
                    if (Point(x, y) in filledPoints) "⬜️" else "◼️"
                }
            }
        }
    }
}

tailrec fun <T> List<T?>.accumulateToGroups(
    remaining: List<T?> = this,
    current: List<T> = emptyList(),
    acc: List<List<T>> = emptyList()
): List<List<T>> {
    return if (remaining.isEmpty()) acc + listOf(current) else {
        val next = remaining.first()
        val tail = remaining.drop(1)
        if (next == null) {
            accumulateToGroups(tail, emptyList(), acc + listOf(current))
        } else {
            accumulateToGroups(tail, current + next, acc)
        }
    }
}

enum class Facing(val vector: Point) {
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
