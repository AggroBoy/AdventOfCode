package twenty25.day05

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2025/day05-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2025/day05.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day05-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day05.txt") }
}

fun puzzle1(fileName: String): Int {
    val lines = File(fileName).readLines()

    val freshIds = lines.subList(0, lines.indexOf("")).map {
        it.substringBefore('-').toLong() to it .substringAfter('-').toLong()
    }
    val ids = lines.subList(freshIds.size + 1, lines.size).map { it.toLong() }

    return ids.count { id ->
        freshIds.any { id >= it.first && id <= it.second }
    }
}

fun puzzle2(fileName: String): Long {
    val lines = File(fileName).readLines()

    val freshIdRanges = lines.subList(0, lines.indexOf("")).map {
        it.substringBefore('-').toLong() to it.substringAfter('-').toLong() + 1
    }.sortedBy { it.first }

    val consolidatedRanges = mutableSetOf<Pair<Long, Long>>()

    for (range in freshIdRanges) {
        // Fully contained or duplicate
        if (consolidatedRanges.any { range.first >= it.first && range.second <= it.second }) continue

        // No overlap
        if (consolidatedRanges.none { range.first <= it.second }) {
            consolidatedRanges.add(range)
            continue
        }

        // Overlap, extend existing range (there should only be one)
        // ranges are sorted by first, so never need to extend first down, only ever second up
        val toExtend = consolidatedRanges.find { range.first <= it.second } ?: throw Exception("Wha?")
        consolidatedRanges.remove(toExtend)
        consolidatedRanges.add(toExtend.first to range.second)
    }

    return consolidatedRanges.sumOf{ (it.second - it.first) }
}
