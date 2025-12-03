package twenty25.day03

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2025/day03-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2025/day03.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day03-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day03.txt") }
}


fun puzzle1(fileName: String): Long {
    val lines = File(fileName).readLines()
    val banks = lines.map { line ->
        line.map { it.toString().toInt() }
    }

    return banks.sumOf { bank ->
        val highest = bank.subList(0, bank.size - 1).sortedDescending()[0]
        val secondHighest = bank.subList(bank.indexOf(highest) + 1, bank.size).sortedDescending()[0]
        (highest * 10L) + secondHighest
    }
}

fun twelveBatteryJoltageForBank(bank: List<Int>): Long {
    var options = bank
    val output = mutableListOf<Int>()

    while (output.size < 12) {
        val maxPossibleIndex = options.size - (11 - output.size)
        val index = options.indexOf( options.subList(0, maxPossibleIndex).max() )
        output.add(options[index])
        options = options.subList(index + 1, options.size)
    }

    return output.joinToString("").toLong()
}

fun puzzle2(fileName: String): Long {
    val lines = File(fileName).readLines()
    val banks = lines.map { line ->
        line.map { it.toString().toInt() }
    }

    val result = banks.sumOf { bank ->
        twelveBatteryJoltageForBank(bank)
    }
    return result
}
