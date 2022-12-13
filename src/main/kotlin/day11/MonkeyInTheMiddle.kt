package day11

import util.Solution

data class Monkey(
    val id: Int,
    val items: List<Long>,
    val op: (old: Long) -> Long,
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
        val operand: Long? = """(\d+)""".toRegex().find(dropped)?.groupValues?.get(1)?.toLong()
        val op: (old: Long) -> Long =
            if (dropped.startsWith("+")) { old -> old + (operand ?: old) }
            else { old -> old * (operand ?: old) }

        return Monkey(
            id = """Monkey (\d):""".toRegex().matchEntire(description[0])!!.groupValues[1].toInt(),
            items = description[1].drop(startingItems.length).split(',').map { item -> item.trim().toLong() },
            op = op,
            divisor = test.matchEntire(description[3])!!.groupValues[1].toInt(),
            targetTrue = testResult.matchEntire(description[4])!!.groupValues[2].toInt(),
            targetFalse = testResult.matchEntire(description[5])!!.groupValues[2].toInt()
        )
    }

    override fun solve1(data: List<String>): Int {
        val monkeyData = data.chunked(6)

        fun playTurn(monkey: Monkey): Pair<Monkey, Map<Int, List<Long>>> {
            val worries: Map<Int, List<Long>> = monkey.items
                .map { monkey.op(it) / 3 }
                .groupBy { if (it % monkey.divisor == 0.toLong()) monkey.targetTrue else monkey.targetFalse }
            return Pair(
                monkey.copy(items = emptyList(), handled = monkey.handled + monkey.items.size),
                worries
            )
        }

        fun updateMonkeys(
            monkeys: List<Monkey>,
            targets: Map<Int, List<Long>>
        ) = monkeys.map { m -> m.copy(items = m.items + (targets[m.id] ?: emptyList())) }

        tailrec fun playRound(monkeys: List<Monkey>, finishedMonkeys: List<Monkey> = emptyList()): List<Monkey> {
            return if (monkeys.isEmpty()) finishedMonkeys else {
                val firstMonkey = monkeys.first()
                val (monkey: Monkey, targets: Map<Int, List<Long>>) = playTurn(firstMonkey)
                assert(monkey.id !in targets)

                playRound(
                    updateMonkeys(monkeys.drop(1), targets),
                    updateMonkeys(finishedMonkeys, targets) + monkey
                )

            }
        }

        tailrec fun play(monkeys: List<Monkey>, roundsPlayed: Int = 0): List<Monkey> {
            return if (roundsPlayed == 20) monkeys else {
                play(playRound(monkeys), roundsPlayed + 1)
            }
        }

        val monkeys: List<Monkey> = monkeyData.map { buildMonkey(it) }
        val play = play(monkeys)
        return play.map { it.handled }.sortedDescending().take(2).fold(1) { acc, h -> acc * h }
    }

    override fun solve2(data: List<String>): Int {
        TODO("Not yet implemented")
    }
}