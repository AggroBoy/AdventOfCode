package twenty24.day01

import util.printTimedOutput
import java.io.File
import kotlin.math.abs

fun main() {
    printTimedOutput("Puzzle 1 ") { puzzle1() }
    printTimedOutput("Puzzle 2 ") { puzzle2() }
}

private fun puzzle1(): Int {
    val (firstList, secondList) = loadLists()

    firstList.sort()
    secondList.sort()

    val distance = firstList.indices.map { i -> abs(firstList[i] - secondList[i]) }.sum()

    return distance
}

private fun puzzle2(): Int {
    val (firstList, secondList) = loadLists()

    val similarity = firstList.map { first ->
        first * secondList.count { it == first }
    }.sum()

    return similarity
}

private fun loadLists(): Pair<MutableList<Int>, MutableList<Int>> {
    val firstList = mutableListOf<Int>()
    val secondList = mutableListOf<Int>()

    File("input/2024/day1.txt").forEachLine {
        val (first, second) = it.split(Regex("\\s+")).map { it.toInt() }
        firstList.add(first)
        secondList.add(second)
    }
    return Pair(firstList, secondList)
}