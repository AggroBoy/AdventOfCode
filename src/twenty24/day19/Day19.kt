package twenty24.day19

import util.Cache
import util.printTimedOutput
import java.io.File


fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2024/day19-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2024/day19.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2024/day19-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2024/day19.txt") }
}

fun puzzle1(fileName: String): Int {
    val lines = File(fileName).readLines()
    val options = lines[0].split(", ")

    val targets = lines.subList(2, lines.size)

    val regEx = options.joinToString(prefix = "^(", separator = "|", postfix = ")+$").toRegex()
    return targets.map { regEx.matches(it) }.count {it }
}

fun puzzle2(fileName: String): Long {
    val lines = File(fileName).readLines()
    val towels = lines[0].split(", ").sortedByDescending { it.length }

    val targets = lines.subList(2, lines.size)

    val cache = Cache<String, Long?>()
    fun countMatches(target: String): Long? {
        if (target.isEmpty())
            return 1

        val options = towels.filter { target.startsWith(it) }
        if (options.isEmpty()) return null

        val result = options.mapNotNull {
            val newTarget = target.substring(it.length)
            cache.getOrStore(newTarget) { countMatches(newTarget) }
        }.sum()

        return result
    }

    return targets.mapNotNull {
        cache.flush()
        countMatches(it)
    }.sum()
}
