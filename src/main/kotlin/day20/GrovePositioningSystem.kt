package day20

import util.Solution

class GrovePositioningSystem(fileName: String) : Solution<Int, Long>(fileName) {
    override fun parse(line: String): Int {
        return line.toInt()
    }

    override fun solve1(data: List<Int>): Long {
        val originalWithIndex: List<IndexedValue<Long>> = data.map { it.toLong() }.withIndex().toList()
        return computeCoordinates(mixList(originalWithIndex, originalWithIndex))
    }

    private fun computeCoordinates(mixedList: List<IndexedValue<Long>>): Long {
        val normalizedList = mixedList.map { it.value }
        val indexOf0 = normalizedList
            .indexOf(0)
        return listOf(1000, 2000, 3000)
            .map { (it + indexOf0) % normalizedList.size }
            .sumOf { normalizedList[it] }
    }

    private fun mixList(
        originalWithIndex: List<IndexedValue<Long>>,
        toMix: List<IndexedValue<Long>>
    ): List<IndexedValue<Long>> {
        val mixedList = originalWithIndex.fold(toMix) { mixedList, indexedValue ->
            val index = mixedList.indexOf(indexedValue)
            val modulus = mixedList.size - 1
            val floorMod = ((index + indexedValue.value) % modulus + modulus) % modulus

            val newList = mixedList - indexedValue

            val left = newList.take(floorMod.toInt())
            val right = newList.drop(floorMod.toInt())

            val indexedValues = left + indexedValue + right
            indexedValues
        }
        return mixedList
    }

    override fun solve2(data: List<Int>): Long {
        val originalWithIndex: List<IndexedValue<Long>> = data
            .map { it * 811589153L }
            .withIndex()
            .toList()

        tailrec fun rep(
            current: Int = 0,
            currentList: List<IndexedValue<Long>> = originalWithIndex
        ): List<IndexedValue<Long>> {
            return if (current == 10) currentList else {
                rep(current + 1, mixList(originalWithIndex, currentList))
            }
        }

        return computeCoordinates(rep())
    }
}
