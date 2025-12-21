package twenty25.day02

import util.isEven
import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 2 test") { puzzle1("input/2025/day02-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle1("input/2025/day02.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day02-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day02.txt") }
}

fun puzzle1(fileName: String): Long {
    val ranges = File(fileName).readLines()[0].split(",")

    val invalidIds = ranges.flatMap { range ->
        val start = range.substringBefore("-")
        val end = range.substringAfter("-")

        val startNumber = start.toLong()
        val endNumber = end.toLong()

        (startNumber..endNumber).map { id ->
            id.toString()
        }.filter {
            it.length.isEven()
        }.map { idString ->
            idString.chunked(idString.length/2)
        }.filter { parts ->
            parts[0] == parts[1]
        }.map { parts ->
            parts.joinToString("").toLong()
        }
    }

    return invalidIds.sum()
}

fun puzzle2(fileName: String): Long {
    val ranges = File(fileName).readLines()[0].split(",")

    val invalidIds = ranges.flatMap { range ->
        val start = range.substringBefore("-")
        val end = range.substringAfter("-")

        val startNumber = start.toLong()
        val endNumber = end.toLong()

        (startNumber..endNumber).map { id ->
            id.toString()
        }.mapNotNull { idString ->
            val isInvalid =
                (1..idString.length/2).filter {
                    idString.length % it == 0
                }.any { size ->
                    val parts = idString.chunked(size)
                    parts.all { it == parts[0] }
                }

            if (isInvalid)
                idString.toLong()
            else
                null
        }
    }

    return invalidIds.sum()
}
