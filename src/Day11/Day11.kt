package Day11

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day11-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day11.txt") }
//    printTimedOutput("Puzzle 2 test") { puzzle2("input/day11-test.txt") }
//    printTimedOutput("Puzzle 2     ") { puzzle2("input/day11.txt") }
}

fun puzzle1(fileName: String): Int {
    var stones = File(fileName).readLines()[0].split(' ').map { it.toLong() }

    repeat(25) {
        stones = stones.flatMap { stone ->
            when {
                stone == 0L -> listOf(1L)
                stone.toString().length.isEven() -> listOf(
                    stone.toString().substring(0, stone.toString().length / 2).toString().toLong(),
                    stone.toString().substring(stone.toString().length / 2).toString().toLong()
                )
                else -> listOf(stone * 2024L)
            }
        }
    }

    return stones.size
}

private fun Int.isEven(): Boolean = this % 2 == 0

fun puzzle2(fileName: String): Int {
    return -1
}
