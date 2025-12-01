package twenty25.day01

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2025/day01-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2025/day01.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day01-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day01.txt") }
}

fun puzzle1(fileName: String): Int {
    val lines = File(fileName).readLines()

    var currentNumber = 50
    var numberOfTimesZero = 0

    for (line in lines) {
        if (line[0] == 'R') {
            currentNumber += line.substring(1).toInt() % 100
        }
        else {
            currentNumber -= line.substring(1).toInt() % 100
        }
        currentNumber %= 100


        if (currentNumber == 0) numberOfTimesZero++
    }

    return numberOfTimesZero
}

fun puzzle2(fileName: String): Int {
    val lines = File(fileName).readLines()

    var currentNumber = 50
    var numberOfTimesZero = 0

    for (line in lines) {
        if (line[0] == 'R') {
            currentNumber += line.substring(1).toInt()
            while (currentNumber >= 100) {
                numberOfTimesZero++
                currentNumber -= 100
            }
        }
        else {
            // If we're already at 0, turning left will always result in a negative, but *this* zero shouldn't count.
            if (currentNumber == 0) numberOfTimesZero--

            currentNumber -= line.substring(1).toInt()

            while (currentNumber < 0) {
                numberOfTimesZero++
                currentNumber += 100
            }

            if (currentNumber == 0)
                numberOfTimesZero++
        }
    }

    return numberOfTimesZero
}
