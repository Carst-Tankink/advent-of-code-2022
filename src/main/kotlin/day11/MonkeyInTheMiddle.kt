package day11

import util.Solution

data class Monkey(
    val id: Int,
    val items: List<Int>,
    val op: (old: Int) -> Int,
    val divisor: Int,
    val targetTrue: Int,
    val targetFalse: Int,
    val handled: Int = 0
)

class MonkeyInTheMiddle(fileName: String) : Solution<String, Int>(fileName) {
    override fun parse(line: String): String? = if (line.isBlank()) null else line.trim()

    private fun buildMonkey(description: List<String>): Monkey {
        val startingItems = "Starting items: "
        val operation = "Operation: new = old "
        val test = """Test: divisible by (\d+)""".toRegex()
        val testResult = """If (true|false): throw to monkey (\d)""".toRegex()
        val dropped = description[2].drop(operation.length)
        val operand: Int? = """(\d+)""".toRegex().find(dropped)?.groupValues?.get(1)?.toInt()
        val op: (old: Int) -> Int =
            if (dropped.startsWith("+")) { old -> old + (operand ?: old) }
            else { old -> old * (operand ?: old) }

        return Monkey(
            id = """Monkey (\d):""".toRegex().matchEntire(description[0])!!.groupValues[1].toInt(),
            items = description[1].drop(startingItems.length).split(',').map { item -> item.trim().toInt() },
            op = op,
            divisor = test.matchEntire(description[3])!!.groupValues[1].toInt(),
            targetTrue = testResult.matchEntire(description[4])!!.groupValues[2].toInt(),
            targetFalse = testResult.matchEntire(description[5])!!.groupValues[2].toInt()
        )
    }

    override fun solve1(data: List<String>): Int {
        val monkeyData = data.chunked(6)
        val monkeys: List<Monkey> = monkeyData.map { buildMonkey(it) }
        TODO("Not yet implemented")
    }

    override fun solve2(data: List<String>): Int {
        TODO("Not yet implemented")
    }
}