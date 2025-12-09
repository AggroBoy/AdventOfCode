package twenty25.day09

import util.Coord
import util.printTimedOutput
import java.io.File
import kotlin.math.absoluteValue

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2025/day09-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2025/day09.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day09-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day09.txt") }
}

fun puzzle1(fileName: String): Long {
    val redTiles = File(fileName).readLines().map { line ->
        val (x,y) = line.split(",")
        Coord(x.toLong(), y.toLong())
    }

    return redTiles.flatMap { tileOne ->
        redTiles.map { tileTwo ->
            // Add one, because the area is inclusive of both coords
            val xDiff = (tileOne.x - tileTwo.x).absoluteValue + 1
            val yDiff = (tileOne.y - tileTwo.y).absoluteValue + 1
            xDiff * yDiff
        }
    }.max()
}

fun puzzle2(fileName: String): Long {
    return 0L
}
