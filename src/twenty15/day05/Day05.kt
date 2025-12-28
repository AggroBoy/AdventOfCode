package twenty15.day05

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2015/day05.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2015/day05.txt") }
}

fun puzzle1(fileName: String): Int {
    val lines = File(fileName).readLines()

    return lines.count {
        isNiceOne(it)
    }
}

fun isNiceOne(str: String): Boolean {
    val naughtyPairs = listOf("ab", "cd", "pq", "xy")
    var repeat = false
    var vowelCount = 0

    str.forEachIndexed { i, c ->
        if (listOf('a', 'e', 'i', 'o', 'u').contains(c)) vowelCount++

        if (i < str.length - 1) {
            if (c == str[i+1]) repeat = true

            if (naughtyPairs.contains("${c}${str[i+1]}")) return false
        }
    }

    return (vowelCount >= 3) && repeat
}

fun puzzle2(fileName: String): Int {
    val lines = File(fileName).readLines()

    return lines.count {
        isNiceTwo(it)
    }
}

fun isNiceTwo(str: String): Boolean {
    var repeat = false
    var pairRepeat = false

    str.forEachIndexed { i, c ->
        if (i >= 2 && c == str[i-2])
            repeat = true

        if (i >= 1) {
            val pair = "${str[i-1]}$c"
            if (str.substring(0, i-1).contains(pair))
                pairRepeat = true
        }
    }

    return repeat && pairRepeat
}
