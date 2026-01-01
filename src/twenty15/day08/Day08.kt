package twenty15.day08

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2015/day08.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2015/day08.txt") }
}

fun puzzle1(fileName: String): Int {
    val lines = File(fileName).readLines()

    return lines.map {
        val new = it.trim('"').replace("\\\\[\"\\\\]".toRegex(), "X").replace("\\\\x[0-9a-f][0-9a-f]".toRegex(), "X")
        it.length - new.length
    }.sum()
}

fun puzzle2(fileName: String): Int {
    val lines = File(fileName).readLines()

    return lines.map {
        val new = it
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
        (new.length + 2) - it.length
    }.sum()
}
