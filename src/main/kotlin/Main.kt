import org.reflections.Reflections
import util.Solution
import java.lang.reflect.Constructor
import java.time.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun main() {
    val day = getCurrentDay(24)
    solveDay(day) { s -> getConstructorOfDay(day).newInstance(s) }
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

@OptIn(ExperimentalTime::class)
private fun <S> runSolution(message: String, function: () -> S) {
    var solution: S
    val time = measureTime {
        solution = function()
    }

    println("$message$solution")
    println("Time: ${time.inWholeMilliseconds}ms (${time.inWholeMicroseconds}µs)")
}
