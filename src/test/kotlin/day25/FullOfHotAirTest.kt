package day25

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FullOfHotAirTest {
    val hotAir = FullOfHotAir("")
    val examples = mapOf(
        1 to "1",
        2 to "2",
        3 to "1=",
        4 to "1-",
        5 to "10",
        6 to "11",
        7 to "12",
        8 to "2=",
        9 to "2-",
        10 to "20",
        15 to "1=0",
        20 to "1-0",
        2022 to "1=11-2",
        12345 to "1-0---0",
        314159265 to "1121-1110-1=0"
    )

    @Test
    fun testSnafuToInt() {
        examples.entries
            .forEach {
                assertEquals(it.key, hotAir.snafuToInt(it.value))
            }
    }

    @Test
    fun testIntToSnafu() {
        examples.entries
            .forEach {
                assertEquals(it.value, hotAir.longToSnafu(it.key), "${it.value}, input ${it.key}")
            }
    }
}
