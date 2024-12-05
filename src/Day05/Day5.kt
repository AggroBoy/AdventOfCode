package Day05

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day5-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day5.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day5-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day5.txt") }
}

fun puzzle1(s: String): Int {
    val (rules: List<Pair<Int, Int>>, manuals: List<List<Int>>) = loadRulesAndManuals(s)

    return manuals
        .filter { manual -> rules.all { rule -> manual.matches(rule) } }
        .map { manual -> manual[manual.size / 2] }
        .sum()
}

fun puzzle2(s: String): Int {
    val (rules: List<Pair<Int, Int>>, manuals: List<List<Int>>) = loadRulesAndManuals(s)

     return manuals
        .filter { manual -> rules.any { rule -> !manual.matches(rule) } }
        .map {
            it.sortedWith { o1, o2 ->
                when {
                    rules.contains(o1 to o2) -> -1
                    rules.contains(o2 to o1) -> 1
                    else -> 0
                }
            }
        }
        .map { manual -> manual[manual.size / 2] }
        .sum()
}

fun List<Int>.matches(rule: Pair<Int, Int>) =
    !this.contains(rule.first) ||
            !this.contains(rule.second) ||
            this.indexOf(rule.first) < this.indexOf(rule.second)

fun loadRulesAndManuals(s: String): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
    val lines = File(s).readLines()

    val separator = lines.indexOf("")

    val ruleLines = lines.subList(0, separator)
    val manualLines = lines.subList(separator + 1, lines.size)

    val rules = ruleLines.map {
        (it.substringBefore('|').toInt() to it.substringAfter('|').toInt())
    }

    val manuals = manualLines.map {
        it.split(",").map { it.toInt() }
    }

    return (rules to manuals)
}
