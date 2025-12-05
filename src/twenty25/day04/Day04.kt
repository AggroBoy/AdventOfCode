package twenty25.day04

import util.Coord
import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2025/day04-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2025/day04.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day04-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day04.txt") }
}

private fun loadPaperCoords(fileName: String): List<Coord> {
    val data = File(fileName).readLines()

    val paperCoords = data.flatMapIndexed { y, row ->
        row.mapIndexed { x, char ->
            if (char == '@') {
                Coord(x, y)
            } else {
                null
            }
        }
    }.filterNotNull()

    return paperCoords
}

fun candidateCanBeMoved(paperCoords: List<Coord>, candidate: Coord): Boolean = paperCoords.count {
    it != candidate &&
            it.x >= candidate.x - 1 && it.x <= candidate.x + 1 &&
            it.y >= candidate.y - 1 && it.y <= candidate.y + 1
} < 4

fun puzzle1(fileName: String): Int {
    val paperCoords = loadPaperCoords(fileName)

    return paperCoords.count { candidate ->
        candidateCanBeMoved(paperCoords, candidate)
    }
}


fun puzzle2(fileName: String): Int {
    val paperCoords = loadPaperCoords(fileName)
    var currentCoords: List<Coord>
    var newCoords = paperCoords
    do {
        currentCoords = newCoords
        newCoords = currentCoords.filterNot { candidateCanBeMoved(currentCoords, it) }
    } while (currentCoords != newCoords)

    return paperCoords.size - newCoords.size
}
