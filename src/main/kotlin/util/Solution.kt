package util

abstract class Solution<Input, SolutionType>(fileName: String) {
    fun star1(): SolutionType = data.solve1()
    fun star2(): SolutionType = data.solve2()

    val data: List<Input> = javaClass
        .getResource(fileName)
        ?.readText()
        ?.lines()
        ?.mapNotNull { parse(it) } ?: emptyList()

    abstract fun parse(line: String): Input?
    abstract fun List<Input>.solve1(): SolutionType
    abstract fun List<Input>.solve2(): SolutionType
}
