package twenty24.day05

import util.printTimedOutput
import java.io.File

typealias Rule = Pair<Int, Int>
typealias Manual = List<Int>

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day5-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day5.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day5-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day5.txt") }
}

fun puzzle1(s: String): Int {
    val (rules: List<Rule>, manuals: List<Manual>) = loadRulesAndManuals(s)

    return manuals
        .filter { manual -> rules.all { rule -> manual.matches(rule) } }
        .map { manual -> manual.middlePage }
        .sum()
}

fun puzzle2(s: String): Int {
    val (rules: List<Rule>, manuals: List<Manual>) = loadRulesAndManuals(s)

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
        .map { manual -> manual.middlePage }
        .sum()
}

fun Manual.matches(rule: Rule) =
    !this.contains(rule.first) ||
            !this.contains(rule.second) ||
            this.indexOf(rule.first) < this.indexOf(rule.second)

val Manual.middlePage
    get() = this[this.size / 2]

fun loadRulesAndManuals(s: String): Pair<List<Rule>, List<Manual>> {
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
