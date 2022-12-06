package day6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TuningTroubleTest {
    private val testClass = TuningTrouble("")

    @Test
    fun testPartOne() {
        assertEquals(7, testClass.solve1(listOf("mjqjpqmgbljsphdztnvjfqwrcgsmlb")))
        assertEquals(5, testClass.solve1(listOf("bvwbjplbgvbhsrlpgdmjqwftvncz")))
        assertEquals(6, testClass.solve1(listOf("nppdvjthqldpwncqszvftbrmjlhg")))
        assertEquals(10, testClass.solve1(listOf("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")))
        assertEquals(11, testClass.solve1(listOf("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")))
    }

    @Test
    fun testPartTwo() {
        assertEquals(19, testClass.solve2(listOf("mjqjpqmgbljsphdztnvjfqwrcgsmlb")))
        assertEquals(23, testClass.solve2(listOf("bvwbjplbgvbhsrlpgdmjqwftvncz")))
        assertEquals(23, testClass.solve2(listOf("nppdvjthqldpwncqszvftbrmjlhg")))
        assertEquals(29, testClass.solve2(listOf("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")))
        assertEquals(26, testClass.solve2(listOf("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")))
    }
}