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

private fun loadPaperCoords(fileName: String): Set<Coord> {
    val data = File(fileName).readLines()

    val paperCoords = data.flatMapIndexed { y, row ->
        row.mapIndexed { x, char ->
            if (char == '@') {
                Coord(x, y)
            } else {
                null
            }
        }
    }.filterNotNull().toSet()

    return paperCoords
}

fun candidateCanBeMoved(paperCoords: Set<Coord>, candidate: Coord): Boolean = paperCoords.count {
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
    var currentCoords: Set<Coord>
    var newCoords = paperCoords
    do {
        currentCoords = newCoords
        newCoords = currentCoords.filterNot { candidateCanBeMoved(currentCoords, it) }.toSet()
    } while (currentCoords != newCoords)

    return paperCoords.size - newCoords.size
}
