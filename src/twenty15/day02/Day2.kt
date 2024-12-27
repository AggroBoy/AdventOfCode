package twenty15.day02

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2015/day2.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2015/day2.txt") }
}

fun puzzle1(fileName: String): Int {
    val lines = File(fileName).readLines()
    return lines.sumOf {
        val dimensions = it.split('x').map { it.toInt() }
        val l = dimensions[0]
        val w = dimensions[1]
        val h = dimensions[2]
        val lw = l * w
        val wh = w * h
        val hl = h * l
        2 * lw + 2 * wh + 2 * hl + minOf(lw, wh, hl)
    }
}

fun puzzle2(fileName: String): Int {
    val lines = File(fileName).readLines()
    return lines.sumOf {
        val dimensions = it.split('x').map { it.toInt() }
        val l = dimensions[0]
        val w = dimensions[1]
        val h = dimensions[2]
        minOf(2 * l + 2 * w, 2 * w + 2 * h, 2 * h + 2 * l) + l * w * h
    }
}
