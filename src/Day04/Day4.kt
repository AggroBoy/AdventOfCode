package Day04

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day4-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day4.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day4-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day4.txt") }
}

fun puzzle1(fileName: String): Int {
    val grid: List<List<Char>> = loadGrid(fileName)

    return grid.flatMapIndexed { y, row ->
        row.indices.map { x ->
            grid.countXmasStartingAtPos(x, y)
        }
    }.sum()
}

fun List<List<Char>>.countXmasStartingAtPos(x: Int, y: Int): Int {
    if (this[y][x] != 'X') {
        return 0
    }
    var count = 0

    if (letterAt(x+1, y) == 'M' && letterAt(x+2, y) == 'A' && letterAt(x+3, y) == 'S') count += 1
    if (letterAt(x+1, y+1) == 'M' && letterAt(x+2, y+2) == 'A' && letterAt(x+3, y+3) == 'S') count += 1
    if (letterAt(x, y+1) == 'M' && letterAt(x, y+2) == 'A' && letterAt(x, y+3) == 'S') count += 1
    if (letterAt(x-1, y+1) == 'M' && letterAt(x-2, y+2) == 'A' && letterAt(x-3, y+3) == 'S') count += 1
    if (letterAt(x-1, y) == 'M' && letterAt(x-2, y) == 'A' && letterAt(x-3, y) == 'S') count += 1
    if (letterAt(x-1, y-1) == 'M' && letterAt(x-2, y-2) == 'A' && letterAt(x-3, y-3) == 'S') count += 1
    if (letterAt(x, y-1) == 'M' && letterAt(x, y-2) == 'A' && letterAt(x, y-3) == 'S') count += 1
    if (letterAt(x+1, y-1) == 'M' && letterAt(x+2, y-2) == 'A' && letterAt(x+3, y-3) == 'S') count += 1

    return count
}

fun List<List<Char>>.letterAt(x: Int, y: Int): Char? {
    if (y < 0 || y >= this.size) {
        return null
    }
    if (x < 0 || x >= this[y].size) {
        return null
    }
    return this[y][x]
}

fun puzzle2(filename: String): Int {
    val grid = loadGrid(filename)

    return grid.flatMapIndexed { y, row ->
        row.indices.map { x -> grid.isXMasCentredAt(x, y) }
    }.count { it }
}

fun List<List<Char>>.isXMasCentredAt(x: Int, y: Int): Boolean {
    val letters = listOf('M', 'S')

    return (letterAt(x, y) == 'A' &&
        letterAt(x-1, y-1) in letters &&
        letterAt(x+1, y-1) in letters &&
        letterAt(x-1, y+1) in letters &&
        letterAt(x+1, y+1) in letters &&
        letterAt(x-1,y-1) != letterAt(x+1, y+1) &&
        letterAt(x-1,y+1) != letterAt(x+1, y-1)
    )
}

private fun loadGrid(fileName: String) = File(fileName).readLines().map { line ->
    line.map {
        it
    }
}
