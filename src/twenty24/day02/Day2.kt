package twenty24.day02

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 ") { puzzle1() }
    printTimedOutput("Puzzle 2 ") { puzzle2() }
}

private fun puzzle1(): Int {
    val reports: List<List<Int>> = readReports()

    val safe = reports.count { isSafe(it) }
    return safe
}

private fun isSafe(it: List<Int>): Boolean {
    if (it[0] == it[1]) return false

    val values = if (it[0] < it[1]) it else it.reversed()

    return values.indices.all { i ->
        when {
            i == 0 -> true
            values[i] - values[i - 1] in 1..3 -> true
            else -> false
        }
    }
}

private fun puzzle2(): Int {
    val reports = readReports()

    val safe = reports.count { isSafe2(it) }

    return safe
}

private fun isSafe2(report: List<Int>): Boolean {
    val permutations = report.indices.map { index -> report.filterIndexed {i, _ -> i != index} }
    return permutations.any { isSafe(it) }
}

private fun readReports(): List<List<Int>> {
    val reports: List<List<Int>> = File("input/2024/day2.txt").readLines().map {
        it.split(" ").map { it.toInt() }
    }
    return reports
}