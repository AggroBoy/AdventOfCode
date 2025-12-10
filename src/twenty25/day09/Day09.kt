package twenty25.day09

import util.Coord
import util.printTimedOutput
import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2025/day09-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2025/day09.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day09-test.txt", Coord(0, -1)) }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day09.txt", Coord(1, 1)) }
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

fun puzzle2(fileName: String, fillOffset: Coord): Long {
    val redTiles = File(fileName).readLines().map { line ->
        val (x,y) = line.split(",")
        Coord(x.toLong(), y.toLong())
    }
    val greenAndRedTiles = mutableSetOf<Coord>()

    // Draw the polygon defined by Red Tiles
    redTiles.indices.forEach { i ->
        val redTileOne = redTiles[i]
        val redTileTwo = when {
            i == redTiles.size-1 -> redTiles[0]
            else -> redTiles[i+1]
        }

        if (redTileOne.x != redTileTwo.x) {
            val maxX = max(redTileOne.x, redTileTwo.x)
            val minX = min(redTileOne.x, redTileTwo.x)
            (minX..maxX).forEach { x ->
                greenAndRedTiles.add(Coord(x, redTileOne.y))
            }
        } else {
            val maxY = max(redTileOne.y, redTileTwo.y)
            val minY = min(redTileOne.y, redTileTwo.y)
            (minY..maxY).forEach { y ->
                greenAndRedTiles.add(Coord(redTileOne.x, y))
            }

        }
    }

    // set up the boundary, immediately outside the polygon
    var boundary = mutableSetOf<Coord>()
    val boundaryY = mutableMapOf<Long, MutableSet<Long>>()
    val boundaryX = mutableMapOf<Long, MutableSet<Long>>()
    var boundaryCandidate: Coord? = redTiles.first() + fillOffset
    while (boundaryCandidate != null) {
        boundary.add(boundaryCandidate)
        boundaryX.getOrPut(boundaryCandidate.x) { mutableSetOf() }.add(boundaryCandidate.y)
        boundaryY.getOrPut(boundaryCandidate.y) { mutableSetOf() }.add(boundaryCandidate.x)

        boundaryCandidate = listOf(
            boundaryCandidate + Coord(0, -1),
            boundaryCandidate + Coord(0, 1),
            boundaryCandidate + Coord(1, 0),
            boundaryCandidate + Coord(-1, 0),
        ).filterNot { boundary.contains(it) }.filterNot { greenAndRedTiles.contains(it) }.find {
            greenAndRedTiles.contains(it + Coord(-1,-1)) ||
            greenAndRedTiles.contains(it + Coord(-1,0)) ||
            greenAndRedTiles.contains(it + Coord(-1,1)) ||
            greenAndRedTiles.contains(it + Coord(0,-1)) ||
            greenAndRedTiles.contains(it + Coord(0,1)) ||
            greenAndRedTiles.contains(it + Coord(1,-1)) ||
            greenAndRedTiles.contains(it + Coord(1,0)) ||
            greenAndRedTiles.contains(it + Coord(1,1))
        }
    }
    boundary = boundary.toHashSet()

    // Ensure that none of the candidate rect's lines hit the boundary
    val validRects = redTiles.flatMap { tileOne ->
        redTiles.map { tileTwo ->
            // Add one, because the area is inclusive of both coords
            val xDiff = (tileOne.x - tileTwo.x).absoluteValue + 1
            val yDiff = (tileOne.y - tileTwo.y).absoluteValue + 1
            (xDiff * yDiff) to (tileOne to tileTwo)
        }
    }.sortedByDescending { it.first }

    validRects.forEach { data ->
        val area = data.first
        val (tileOne, tileTwo) = data.second

        val xSequence = min(tileOne.x, tileTwo.x)..max(tileOne.x, tileTwo.x)
        val yMapOne = boundaryY.getValue(tileOne.y)
        val yMapTwo = boundaryY.getValue(tileTwo.y)
        val xValid = xSequence.none {
            yMapOne.contains(it) || yMapTwo.contains(it)
        }
        if (xValid) {
            val ySequence = min(tileOne.y, tileTwo.y)..max(tileOne.y, tileTwo.y)
            val xMapOne = boundaryX.getValue(tileOne.x)
            val xMapTwo = boundaryX.getValue(tileTwo.x)
            val yValid = ySequence.none {
                xMapOne.contains(it) || xMapTwo.contains(it)
            }
            if (yValid)
                return area
        }
    }
    return 0L
}
