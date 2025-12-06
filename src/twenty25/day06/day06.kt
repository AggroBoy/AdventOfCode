package twenty25.day06

import util.printTimedOutput
import java.io.File
import kotlin.collections.reduce

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2025/day06-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2025/day06.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day06-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day06.txt") }
}

fun puzzle1(fileName: String): Long {
    val lines = File(fileName).readLines().map { line ->
        line.split(" ").filter { it.isNotEmpty()}
    }

    return lines[1].indices.sumOf { i ->
        val numbers = lines.subList(0, lines.size - 1).map { it[i].toLong() }
        val operation = lines.last()[i]
        when (operation) {
            "+" -> numbers.reduce { acc, num -> acc + num }
            "*" -> numbers.reduce { acc, num -> acc * num }
            else -> throw Exception("Nope!")
        }
    }
}

fun puzzle2(fileName: String): Long {
    // adding a space to the end avoids needing special case processing for the last column later
    val lines = File(fileName).readLines().map { "$it " }
    val columns = mutableListOf<List<String>>()

    var columnStart = 0
    lines[1].indices.forEach { i ->
        if (lines.all {it[i] == ' '}) {
            columns.add( lines.map { it.substring(columnStart, i)} )
            columnStart = i+1
        }
    }

    return columns.sumOf { column ->
        val numbers = column[0].indices.map { i ->
            column.subList(0, column.size-1).map { it[i] }
                .joinToString("")
                .trim()
                .toLong()
        }
        val operation = column.last().trim()

        when (operation) {
            "+" -> numbers.reduce { acc, num -> acc + num }
            "*" -> numbers.reduce { acc, num -> acc * num }
            else -> throw Exception("Nope!")
        }
    }
}
