import org.reflections.Reflections
import util.Solution
import java.lang.reflect.Constructor
import java.time.LocalDateTime

fun main() {
    solveDay(getCurrentDay()) { s -> getConstructorOfDay(getCurrentDay()).newInstance(s) }
}

private fun getCurrentDay(override: Int? = null): Int {
    return override ?: LocalDateTime.now().dayOfMonth
}

private fun getConstructorOfDay(day: Int): Constructor<out Solution<*, *>> {
    val reflections = Reflections("day$day")
    return reflections.getSubTypesOf(Solution::class.java).first().getConstructor(String::class.java)
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
