package twenty25.day02

import twenty22.day13.parseList
import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 2 test") { puzzle1("input/2025/day02-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle1("input/2025/day02.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day02-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day02.txt") }
}

fun Int.isOdd() = this % 2 != 0
fun Int.isEven() = this % 2 == 0

fun puzzle1(fileName: String): Long {
    val ranges = File(fileName).readLines()[0].split(",")
    val answer = ranges.flatMap { range ->
        val start = range.substringBefore("-")
        val end = range.substringAfter("-")

        val startNumber = start.toLong()
        val endNumber = end.toLong()

        (startNumber..endNumber).map { number ->
            number.toString()
        }.filter {
            it.length.isEven()
        }.map { numberString ->
            numberString.chunked(numberString.length/2)
        }.filter { parts ->
            parts.toSet().size == 1
        }.map { parts ->
            parts.joinToString("").toLong()
        }
    }
    return answer.sum()
}

fun puzzle2(fileName: String): Long {
    val ranges = File(fileName).readLines()[0].split(",")
    val answer = ranges.flatMap { range ->
        val start = range.substringBefore("-")
        val end = range.substringAfter("-")

        val startNumber = start.toLong()
        val endNumber = end.toLong()

        (startNumber..endNumber).map { number ->
            number.toString()
        }.flatMap { numberString ->
            (1..numberString.length/2).filter {
                numberString.length % it == 0
            }.map { size ->
                numberString.chunked(size)
            }.filter{ parts ->
                parts.toSet().size == 1
            }.map { parts ->
                parts.joinToString("").toLong()
            }
        }
    }.toSet()
    return answer.sum()
}
