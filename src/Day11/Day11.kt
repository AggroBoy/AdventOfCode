package Day11

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle("input/day11-test.txt", 25) }
    printTimedOutput("Puzzle 1     ") { puzzle("input/day11.txt", 25) }
    printTimedOutput("Puzzle 2 test") { puzzle("input/day11-test.txt", 75) }
    printTimedOutput("Puzzle 2     ") { puzzle("input/day11.txt", 75) }
}

val cache: MutableMap<Pair<Long, Int>, Long> = mutableMapOf()

fun puzzle(fileName: String, blinks: Int): Long {
    val stones = File(fileName).readLines()[0].split(' ').map { it.toLong() }

    return stones.map { stone -> getFinalStoneCountForStone(stone, blinks) }.sum()
}

fun getFinalStoneCountForStone(stone: Long, blinks: Int): Long {
    cache[stone to blinks]?.let { return it }

    if (blinks == 0) return 1

    val stoneString = stone.toString()
    return when {
        stone == 0L -> getFinalStoneCountForStone(1, blinks - 1)
        stoneString.length.isEven() -> {
            val half = stoneString.length / 2
            val left = stoneString.substring(0, half).toLong()
            val right = stoneString.substring(half).toLong()
            getFinalStoneCountForStone(left, blinks - 1) + getFinalStoneCountForStone(right, blinks - 1)
        }
        else -> getFinalStoneCountForStone(stone * 2024, blinks - 1)
    }.also {
        cache[stone to blinks] = it
    }
}

private fun Int.isEven(): Boolean = this % 2 == 0
