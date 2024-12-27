package twenty15.day0

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2015/day1.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2015/day1.txt") }
}

fun puzzle1(fileName: String): Int {
    var instructions = File(fileName).readLines().joinToString("")

    return instructions.count { it == '(' } - instructions.count { it == ')' }
}

fun puzzle2(fileName: String): Int {
    var instructions = File(fileName).readLines().joinToString("")

    var floor = 0
    instructions.forEachIndexed { index, c ->
        floor += if (c == '(') 1 else -1
        if (floor == -1) return index + 1
    }
    return -1
}
