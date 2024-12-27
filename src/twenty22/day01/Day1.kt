package twenty22.day01

import java.io.File

fun main(args: Array<String>) {
    val elves = HashMap<Int, Int>()
    var elf = 1
    var calories = 0

    File("input/2022/day1.txt").forEachLine {
        if (it == "") {
            elves[elf] = calories
            elf++
            calories = 0
        } else {
            calories += it.toInt()
        }
    }

    println("Puzzle 1: ${elves.maxBy { it.value }}")

    println("Puzzle 2: ${elves.values.sortedDescending().subList(0, 3).sum()}")
}