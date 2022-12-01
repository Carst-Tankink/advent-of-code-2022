import org.reflections.Reflections
import util.Solution
import java.time.LocalDateTime

fun getCurrentDay(override: Int? = null): Int {
    return override ?: LocalDateTime.now().dayOfMonth
}

fun getClassOfDay(day: Int): Class<out Solution<*, *>> {
    val reflections = Reflections("day$day")

    val solutions = reflections.getSubTypesOf(Solution::class.java)
    println("Possible solutions: $solutions")

    return solutions.first()
}

fun main() {
    val day = getCurrentDay()
    println("Today is $day")
    val constructor = getClassOfDay(day).getConstructor(String::class.java)
    solveDay(day) { s -> constructor.newInstance(s) }
}

private fun <I, S> solveDay(day: Int, constructor: (String) -> Solution<I, S>) {
    val dayPrefix = "/day$day/"
    val sample = constructor("${dayPrefix}sample")
    val input = constructor("${dayPrefix}input")

    runSolution("Sample star 1: ") { sample.star1() }
    runSolution("Input star 1: ") { input.star1() }

    runSolution("Sample star 2: ") { sample.star2() }
    runSolution("Input star 2: ") { input.star2() }
}

private fun <S> runSolution(message: String, function: () -> S) {
    val before = System.currentTimeMillis()
    val solution = function()
    val after = System.currentTimeMillis()
    println("$message$solution\nTime: ${after - before}ms")
}
