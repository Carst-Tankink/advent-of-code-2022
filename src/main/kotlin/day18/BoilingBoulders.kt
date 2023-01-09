package day18

import util.Point3D
import util.Solution

class BoilingBoulders(fileName: String) : Solution<Point3D, Long>(fileName) {
    override fun parse(line: String): Point3D {
        val (x, y, z) = line.split(",").map { it.toLong() }
        return Point3D(x, y, z)
    }

    override fun solve1(data: List<Point3D>): Long {
        val points = data.toSet()

        return points.sumOf {
            it.neighbours().count() { side -> side !in points }
        }.toLong()
    }

    override fun solve2(data: List<Point3D>): Long {
        val points = data.toSet()
        val minX = points.minOf { it.x }
        val maxX = points.maxOf { it.x }
        val minY = points.minOf { it.y }
        val maxY = points.maxOf { it.y }
        val minZ = points.minOf { it.z }
        val maxZ = points.maxOf { it.z }

        println("Bound by: ($minX, $minY, $minZ); ($maxX, $maxY, $maxZ)")
        val surfaces = points.sumOf {
            it.neighbours().count { side -> side !in points }
        }.toLong()

        val allInside = points.count() {
            it.neighbours().all { side -> side in points }
        }.toLong()

        return surfaces - allInside * 6
    }
}
