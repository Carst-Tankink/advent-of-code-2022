package day12

import util.Grid
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

class HillClimbingAlgorithm(fileName: String) : Solution<List<Char>, Int>(fileName) {
    override fun parse(line: String): List<Char> {
        return line.map { it }
    }


    override fun solve1(data: List<List<Char>>): Int {
        val heightMap = data.toGrid()
        val start = heightMap.entries.first { it.value == 'S' }.key
        return getShortestPathFromStart(heightMap, setOf(start))
    }

    private fun getShortestPathFromStart(heightMap: Grid<Char>, starts: Set<Point>): Int {
        val end = heightMap.entries.first { it.value == 'E' }.key

        fun getHeight(v: Point) = if (heightMap[v] == 'S') 'a'.code else if (v == end) 'z'.code else (heightMap[v]?.code ?: 200)

        tailrec fun shortestPath(
            unvisited: Set<Point> = heightMap.keys,
            distances: Map<Point, Int> = starts.associateWith { 0 }
        ): Map<Point, Int> {
            return if (distances.filterKeys { it in unvisited }.isEmpty() || end in distances.keys) distances else {
                val v = distances.filterKeys { it in unvisited }.minBy { it.value }.key
                val distV = distances[v]!!
                val currentHeight = getHeight(v)
                val neighbors = v.getNeighbours(cardinal = true)
                    .filter { it in unvisited }
                    .filter { (getHeight(it)) <= currentHeight + 1 }

                val associateWith = neighbors.associateWith {
                    val currDist = distances[it] ?: Int.MAX_VALUE
                    val newDist = if (distV + 1 < currDist) distV + 1 else currDist
                    newDist
                }
                val newDistances = distances + associateWith
                val unvisited1 = unvisited - v
                shortestPath(unvisited1, newDistances)
            }

        }

        return shortestPath()[end] ?: Int.MAX_VALUE
    }

    override fun solve2(data: List<List<Char>>): Int {
        val heightMap = data.toGrid()
        val starts = heightMap.filterValues { it == 'S' || it == 'a' }.keys
        
        return getShortestPathFromStart(heightMap, starts)
    }
}