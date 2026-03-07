package twenty15.day11

import util.printTimedOutput
import java.io.File

fun main() {
    // I didn't write code for this one; it was easier to figure out by hand

    printTimedOutput("Puzzle 1 test") { puzzle1("input/2015/day11-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2015/day11.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2015/day11-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2015/day11.txt") }
}

fun puzzle1(fileName: String): Int {
    val lines = File(fileName).readLines()

    return 0
}

fun puzzle2(fileName: String): Int {
    val lines = File(fileName).readLines()

    return 0
}
