package twenty15.day10

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1     ") { puzzle1("1321131112") }
    printTimedOutput("Puzzle 2     ") { puzzle2("1321131112") }
}

fun puzzle1(input: String): Int {

    var work = input

    for (i in 1..40) {
        work = lookSay(work)
    }

    return work.length
}

fun lookSay(input: String): String {
    val output = StringBuilder()
    var currentChar: Char? = null
    var count = 0
    input.indices.forEach { i ->
        if (currentChar == input[i])
            count++
        else {
            if (currentChar != null) output.append("${count}$currentChar")
            count = 1
            currentChar = input[i]
        }
    }
    output.append("${count}$currentChar")

    return output.toString()
}

fun puzzle2(input: String): Int {
    var work = input

    for (i in 1..50) {
        work = lookSay(work)
    }

    return work.length
}
