package Day03

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 ") { puzzle1() }
    printTimedOutput("Puzzle 2 ") { puzzle2() }
}

fun puzzle1(): Int {
    return File("input/day3.txt").readLines().flatMap {
        Regex("""mul\((\d\d?\d?),(\d\d?\d?)\)""").findAll(it).map { match ->
            match.groupValues[1].toInt() * match.groupValues[2].toInt()
        }
    }.sum()
}

fun puzzle2(): Int {
    val input = File("input/day3.txt")
        .readLines()
        .joinToString()
        .replace(Regex("""don't\(\).*?(do\(\)|$)"""), "")

    return Regex("""mul\((\d\d?\d?),(\d\d?\d?)\)""").findAll(input).map { match ->
        match.groupValues[1].toInt() * match.groupValues[2].toInt()
    }.sum()
}
