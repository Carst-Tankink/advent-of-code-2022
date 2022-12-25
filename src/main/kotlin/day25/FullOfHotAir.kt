package day25

import util.Solution

class FullOfHotAir(fileName: String) : Solution<String, String>(fileName) {
    override fun parse(line: String): String {
        return line
    }

    override fun solve1(data: List<String>): String {
        val final = data.sumOf { snafuToInt(it) }

        return longToSnafu(final)
    }

    override fun solve2(data: List<String>): String {
        TODO("Not yet implemented")
    }

    tailrec fun snafuToInt(snafu: String, power: Int = 0, current: Long = 0): Long {
        return if (snafu.isEmpty()) current else {
            val acc = when (val last = snafu.last()) {
                '2' -> current + 2 * (5.pow(power))
                '1' -> current + 5.pow(power)
                '0' -> current
                '-' -> current - 5.pow(power)
                '=' -> current - 2 * 5.pow(power)
                else -> TODO("Unknown digit")
            }

            snafuToInt(snafu.dropLast(1), power + 1, acc)
        }
    }

    fun longToSnafu(int: Long, acc: String = ""): String {
        return if (int == 0L) acc else {
            val div = int / 5
            when (val mod = int % 5) {
                0L -> longToSnafu(div, "0$acc")
                1L -> longToSnafu(div, "1$acc")
                2L -> longToSnafu(div, "2$acc")
                3L -> longToSnafu(div + 1, "=$acc")
                4L -> longToSnafu(div + 1, "-$acc")
                else -> TODO("Modulo doesn't go that far $mod ")
            }
        }
    }

    private fun Int.pow(to: Int): Long {
        return if (to == 0) 1 else {
            this * this.pow(to - 1)
        }
    }
}
