package Day19

import util.printTimedOutput
import java.io.File
import kotlin.math.max


fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day19-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day19.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day19-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day19.txt") }
}

fun puzzle1(fileName: String): Int {
    val lines = File(fileName).readLines()
    val options = lines[0].split(", ")

    val targets = lines.subList(2, lines.size)

    val regEx = options.joinToString(prefix = "^(", separator = "|", postfix = ")+$").toRegex()
    return targets.map { regEx.matches(it) }.count {it }
}

fun findMatches(target: String, towels: List<String>, progress: List<String> = emptyList()): List<List<String>>? {
    if (target.isEmpty()) return listOf(progress)

    val options = towels.filter { target.startsWith(it) }
    if (options.isEmpty()) return null
    return options.map { option ->
        findMatches(target.substring(option.length), towels, progress + option)
    }.filterNotNull().flatten()
}

fun optimiseTowels(towels: List<String>): Map<String, Int> {
    val result: MutableMap<String, Int> = mutableMapOf()
    val subTowels: MutableSet<String> = mutableSetOf()

//    towels.forEach { current ->
//        val matches = findMatches(current, towels - current)
//        result[current] = matches?.size?.plus(1) ?: 1
//    }

    return towels.map { current ->
        current to (findMatches(current, towels - current)?.size?.plus(1) ?: 0)
    }.toMap()
}

fun puzzle2(fileName: String): Int {
    val lines = File(fileName).readLines()
    val towels = optimiseTowels(lines[0].split(", "))

    val targets = lines.subList(2, lines.size)
    val pointsSeen = mutableSetOf<String>()
    val scoring = mutableSetOf<Pair<String, Int>>()

    fun countMatches(target: String, progress: List<String> = emptyList()): Boolean {
        val progressString = progress.joinToString("")

        if (target.isEmpty())
            return true

        if (pointsSeen.contains(progressString)) return false
        pointsSeen.add(progressString)

        val options = towels.keys.filter { target.startsWith(it) }.sortedByDescending { it.length }
        if (options.isEmpty()) return false

        val result = options.map {
            if (countMatches(target.substring(it.length), progress + it)) {
                scoring.add(it to progressString.length)
                true
            } else {
                false
            }
        }.any{ it }
        return result
    }

    val bar = countMatches("bbrgwb")

    val foo = targets.map {
        pointsSeen.clear();
        scoring.clear()
        if (countMatches(it)) {
            max(1, scoring.map { (key, value) -> towels[key]!! }.sum())
        } else {
            0
        }
    }.sum()
    return foo
}
