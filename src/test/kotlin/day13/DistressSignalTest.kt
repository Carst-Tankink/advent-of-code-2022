package day13

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DistressSignalTest {
    val distressSignal = DistressSignal("")

    @Test
    fun testSingletonList() {
        assertEquals(NestedList(listOf(Number(1))), distressSignal.parseLine("[1]")?.first)
    }

    @Test
    fun testNestedList() {
        assertEquals(
            NestedList(listOf(
                NestedList(listOf(Number(1))),
                NestedList(emptyList())
            )),
            distressSignal.parseLine("[[1],[]]")?.first)
    }
}