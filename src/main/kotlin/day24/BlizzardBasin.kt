package day24

import util.Facing

sealed interface POI
data class Blizzard(val facing: Facing)
class BlizzardBasin {
}