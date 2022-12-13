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

private fun List<Long>.product(): Long = this.fold(1.toLong()) {acc, l -> acc * l}

class MonkeyInTheMiddle(fileName: String) : Solution<String, Long>(fileName) {
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

    private fun playTurn(monkey: Monkey, modulus: Long, isPart2: Boolean = false): Pair<Monkey, Map<Int, List<Long>>> {
        val worries: Map<Int, List<Long>> = monkey.items
            .map { monkey.op(it) % modulus }
            .map { if (isPart2) it else it / 3 }
            .groupBy { if (it % monkey.divisor == 0.toLong()) monkey.targetTrue else monkey.targetFalse }
        return Pair(
            monkey.copy(items = emptyList(), handled = monkey.handled + monkey.items.size),
            worries
        )
    }

    private tailrec fun playRound(monkeys: List<Monkey>, finishedMonkeys: List<Monkey> = emptyList(), modulus: Long, isPart2: Boolean = false): List<Monkey> {
        return if (monkeys.isEmpty()) finishedMonkeys else {
            val firstMonkey = monkeys.first()
            val (monkey: Monkey, targets: Map<Int, List<Long>>) = playTurn(firstMonkey, modulus, isPart2)
            assert(monkey.id !in targets)

            playRound(
                updateMonkeys(monkeys.drop(1), targets),
                updateMonkeys(finishedMonkeys, targets) + monkey,
                modulus,
                isPart2 = isPart2
            )

        }
    }

    private tailrec fun play(monkeys: List<Monkey>, roundsPlayed: Int = 0, isPart2: Boolean = false): List<Monkey> {
        val target = if (isPart2) 10000 else 20
        val modulus = monkeys.map { it.divisor.toLong() }.product()
        return if (roundsPlayed == target) monkeys else {
            play(playRound(monkeys, modulus = modulus, isPart2 = true), roundsPlayed + 1, isPart2)
        }
    }

    private fun updateMonkeys(
        monkeys: List<Monkey>,
        targets: Map<Int, List<Long>>
    ) = monkeys.map { m -> m.copy(items = m.items + (targets[m.id] ?: emptyList())) }

    override fun solve1(data: List<String>): Long {
        val monkeyData = data.chunked(6)
        val monkeys: List<Monkey> = monkeyData.map { buildMonkey(it) }
        val play = play(monkeys)
        return play.map { it.handled }.sortedDescending().take(2).fold(1) { acc, h -> acc * h }
    }

    override fun solve2(data: List<String>): Long {
        val monkeyData = data.chunked(6)
        val monkeys: List<Monkey> = monkeyData.map { buildMonkey(it) }
        val play = play(monkeys, isPart2 = true)
        return play.map { it.handled }.sortedDescending().take(2).fold(1) { acc, h -> acc * h }
    }
}