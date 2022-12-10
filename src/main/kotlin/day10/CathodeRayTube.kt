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

    override fun solve1(data: List<Ins>): Int {

        tailrec fun calculate(
            commands: List<Ins>,
            x: Int = 1,
            cycle: Int = 1,
            signalStrength: Int = 0,
            inFlight: Int? = null
        ): Int {
            return if (commands.isEmpty() || cycle > 220) signalStrength else {
                val newStrength = signalStrength + (if (cycle % 40 == 20) (cycle * x) else 0)

                if (inFlight == null) {
                    val head = commands.first()
                    val tail = commands.drop(1)
                    val newInflight = if (head is AddX) head.amount else null
                    calculate(tail, x, cycle + 1, newStrength, newInflight)
                } else {
                    val newX = x + inFlight
                    calculate(commands, newX, cycle + 1, newStrength)
                }
            }
        }

        return calculate(data)
    }

    override fun solve2(data: List<Ins>): Int {
        TODO("Not yet implemented")
    }

}