package day21

import util.Solution
import java.math.BigInteger

sealed class Monkey(val id: String)
class NumberMonkey(id: String, val number: Long) : Monkey(id)
class OperatorMonkey(id: String, val operator: Char, val leftOperand: String, val rightOperand: String) : Monkey(id) {
    override fun toString(): String {
        return "$leftOperand $operator $rightOperand"
    }
}

class MonkeyMath(fileName: String) : Solution<Monkey, Long>(fileName) {
    override fun parse(line: String): Monkey {
        val (id, rest) = line.split(':').map { it.trim() }
        val numberRegex = """(\d+)""".toRegex()
        return if (numberRegex.matches(rest)) {
            NumberMonkey(id, numberRegex.matchEntire(rest)!!.groupValues[1].toLong())
        } else {
            val (left, operator, right) = rest.split(' ')
            OperatorMonkey(id, operator.first(), left, right)
        }
    }

    private fun eval(monkeyId: String, monkeys: Map<String, Monkey>, humanNumber: BigInteger? = null): BigInteger {
        return when (val monkey = monkeys[monkeyId]!!) {
            is NumberMonkey -> if (monkey.id == "humn") humanNumber
                ?: monkey.number.toBigInteger() else monkey.number.toBigInteger()

            is OperatorMonkey -> {
                val left = eval(monkey.leftOperand, monkeys, humanNumber)
                val right = eval(monkey.rightOperand, monkeys, humanNumber)
                when (monkey.operator) {
                    '+' -> left + right
                    '-' -> left - right
                    '/' -> left / right
                    '*' -> left * right
                    else -> TODO("Unknown operator ${monkey.operator}")
                }
            }
        }
    }

    override fun solve1(data: List<Monkey>): Long {
        return eval("root", data.associateBy { it.id }).toLong()
    }

    override fun solve2(data: List<Monkey>): Long {
        val monkeys = data.associateBy { it.id }
        fun hasHuman(monkeyId: String): Boolean {
            val monkey = monkeys[monkeyId]!!
            return when {
                monkeyId == "humn" -> true
                monkey is NumberMonkey -> false
                monkey is OperatorMonkey -> hasHuman(monkey.leftOperand) || hasHuman(monkey.rightOperand)
                else -> TODO("Uncovered case")
            }
        }

        fun humanShout(lhs: BigInteger, rhs: String): BigInteger {
            return if (rhs == "humn") lhs else {
                val operation =
                    monkeys[rhs]!! as OperatorMonkey // This should succeed, there should be no numbers in the rhs, as we eval them immediately
                val leftHasHuman = hasHuman(operation.leftOperand)
                val value =
                    if (leftHasHuman) eval(operation.rightOperand, monkeys) else eval(operation.leftOperand, monkeys)
                val toSolve = when (operation.operator) {
                    '+' -> lhs - value
                    '-' -> if (leftHasHuman) lhs + value else value - lhs
                    '*' -> {
                        val l = lhs / value
                        assert(l * value == lhs) {"Integer division error, mult: lhs = $lhs, l = $l  value = $value"}
                        l
                    }

                    '/' -> if (leftHasHuman) lhs * value else {
                        val l = value / lhs
                        assert(l * lhs == value) {"Integer division error, div: lhs = $lhs, l = $l, value = $value"}
                        l
                    }

                    else -> TODO("Unknown operator $operation")
                }

                val solution = humanShout(toSolve, if (leftHasHuman) operation.leftOperand else operation.rightOperand)
                val pluggedSolution = eval(rhs, monkeys, solution)
                if (lhs != pluggedSolution) {
                    println("Failed to solve solution at equation ${monkeys[rhs]}. Was $pluggedSolution, should be $lhs")
                }
                solution
            }
        }

        val rootEquation = monkeys["root"]!! as OperatorMonkey
        val leftHasHuman = hasHuman(rootEquation.leftOperand)
        val (lhs: BigInteger, rhs: String) = if (leftHasHuman) Pair(
            eval(rootEquation.rightOperand, monkeys),
            rootEquation.leftOperand
        ) else Pair(
            eval(rootEquation.leftOperand, monkeys),
            rootEquation.rightOperand
        )

        val humanShout = humanShout(lhs, rhs)

        println("Shout $humanShout")
        println("Value: ${eval(rhs, monkeys, humanShout)}")

        return humanShout.toLong()
    }
}