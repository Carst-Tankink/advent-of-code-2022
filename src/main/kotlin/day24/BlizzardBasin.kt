package day24

import util.Facing
import util.Solution

sealed interface POI
data class Blizzard(val facing: Facing) : POI
object Wall : POI
object Open : POI

class BlizzardBasin(fileName: String) : Solution<List<POI>, Int>(fileName) {
    override fun parse(line: String): List<POI> {
        return line.map {
            when (it) {
                '#' -> Wall
                '.' -> Open
                '<' -> Blizzard(Facing.LEFT)
                '>' -> Blizzard(Facing.RIGHT)
                '^' -> Blizzard(Facing.UP)
                'v' -> Blizzard(Facing.DOWN)
                else -> TODO("Unknown character $it")
            }
        }
    }

    override fun solve1(data: List<List<POI>>): Int {
        TODO("Not yet implemented")
    }

    override fun solve2(data: List<List<POI>>): Int {
        TODO("Not yet implemented")
    }
}
