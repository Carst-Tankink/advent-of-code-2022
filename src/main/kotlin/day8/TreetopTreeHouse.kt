package day8

import util.Helpers.Companion.transpose
import util.Point
import util.Solution

class TreetopTreeHouse(fileName: String) : Solution<List<Int>, Int>(fileName) {
    override fun parse(line: String): List<Int> = line.map { it.toString().toInt() }

    override fun solve1(data: List<List<Int>>): Int {
        val coordinated: List<List<Pair<Point, Int>>> = data.mapIndexed { y, r ->
            r.mapIndexed { x, p -> Pair(Point(x.toLong(), y.toLong()), p) }
        }

        val visibleHorizontal = getVisible(coordinated)

        val columns = coordinated.transpose()
        val visibleVertical = getVisible(columns)


        val visible = visibleHorizontal.union(visibleVertical)
        println("Visible: $visible")
        

        return visible.size
    }

    private fun getVisible(coordinated: List<List<Pair<Point, Int>>>): Set<Point> {
        data class FoldState(val largest: Int, val visible: Set<Point>)

        val initialState = FoldState(-1, emptySet())
        val visibleFromLeft = coordinated.flatMap { row ->
            row.fold(initialState) { state, location ->
                if (location.second > state.largest) {
                    FoldState(largest = location.second, state.visible + location.first)
                } else {
                    state
                }
            }.visible
        }

        val visibleFromRight = coordinated.flatMap { row ->
            row.foldRight(initialState) { location, state ->
                if (location.second > state.largest) {
                    FoldState(largest = location.second, state.visible + location.first)
                } else {
                    state
                }
            }.visible
        }
        
        return visibleFromLeft.union(visibleFromRight)
    }

    override fun solve2(data: List<List<Int>>): Int {
        TODO("Not yet implemented")
    }
}