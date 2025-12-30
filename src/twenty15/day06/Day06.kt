package twenty15.day06

import util.printTimedOutput
import java.io.File
import kotlin.math.max

fun main() {
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2015/day06.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2015/day06.txt") }
}

fun puzzle1(fileName: String): Int {
    val lights: MutableList<MutableList<Boolean>> = MutableList(1000) { MutableList(1000) { false } }
    val lines = File(fileName).readLines()

    lines.forEach { line ->
        val match = "(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)".toRegex().matchEntire(line) ?: throw IllegalArgumentException()
        val operation = match.groups[1]?.value ?: throw Exception("missing group")
        val x1 = match.groups[2]?.value?.toInt() ?: throw Exception("missing group")
        val y1 = match.groups[3]?.value?.toInt() ?: throw Exception("missing group")
        val x2 = match.groups[4]?.value?.toInt() ?: throw Exception("missing group")
        val y2 = match.groups[5]?.value?.toInt() ?: throw Exception("missing group")

        for (x in x1..x2) {
            for (y in y1..y2) {
                lights[x][y] = when (operation) {
                    "turn on" -> true
                    "turn off" -> false
                    "toggle" -> !lights[x][y]
                    else -> throw Exception("invalid operation")
                }
            }
        }
    }

    return lights.sumOf { row ->
        row.count { it }
    }
}

fun puzzle2(fileName: String): Int {
    val lights: MutableList<MutableList<Int>> = MutableList(1000) { MutableList(1000) { 0 } }
    val lines = File(fileName).readLines()

    lines.forEach { line ->
        val match = "(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)".toRegex().matchEntire(line) ?: throw IllegalArgumentException()
        val operation = match.groups[1]?.value ?: throw Exception("missing group")
        val x1 = match.groups[2]?.value?.toInt() ?: throw Exception("missing group")
        val y1 = match.groups[3]?.value?.toInt() ?: throw Exception("missing group")
        val x2 = match.groups[4]?.value?.toInt() ?: throw Exception("missing group")
        val y2 = match.groups[5]?.value?.toInt() ?: throw Exception("missing group")

        for (x in x1..x2) {
            for (y in y1..y2) {
                lights[x][y] = when (operation) {
                    "turn on" -> lights[x][y] + 1
                    "turn off" -> max(lights[x][y] - 1, 0)
                    "toggle" -> lights[x][y] + 2
                    else -> throw Exception("invalid operation")
                }
            }
        }
    }

    return lights.sumOf { row ->
        row.sum()
    }
}
