package day10

import util.Solution

sealed interface Ins
data class AddX(val amount: Int) : Ins
object NoOp : Ins
class CathodeRayTube(fileName: String) : Solution<Ins, Int>(fileName) {
    override fun parse(line: String): Ins {
        return if (line == "noop") NoOp else {
            AddX(amount = line.drop("addX ".length).toInt())
        }
    }

    private tailrec fun calculate(
        commands: List<Ins>,
        x: Int = 1,
        cycles: List<Int> = listOf(1),
        inFlight: Int? = null
    ): List<Int> {
        return if (commands.isEmpty()) cycles else {
            if (inFlight == null) {
                val head = commands.first()
                val tail = commands.drop(1)
                val newInflight = if (head is AddX) head.amount else null
                calculate(tail, x, cycles + x, newInflight)
            } else {
                val newX = x + inFlight
                calculate(commands, newX, cycles + newX)
            }
        }
    }

    override fun solve1(data: List<Ins>): Int {
        return calculate(data).mapIndexed { index, v -> if ((index + 1) % 40 == 20) v * (index + 1) else 0 }.sum()
    }

    override fun solve2(data: List<Ins>): Int {
        val sprites = calculate(data)

        val points = (0 until 6).joinToString("\n") { y ->
            (0 until 40).joinToString("") { x ->
                val position = y * 40 + x
                val sprite = sprites[position]
                if (sprite - 1 <= x && x <= sprite + 1) "⬜️" else "◼️"
            }
        }

        println(points)
        return -1
    }

}