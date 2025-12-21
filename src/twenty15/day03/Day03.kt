package twenty15.day03

import util.Coord
import util.Direction
import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2015/day03.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2015/day03.txt") }
}

fun puzzle1(fileName: String): Int {
    val commands = File(fileName).readLines().first().map { when(it) {
        '^' -> Direction.UP
        'v' -> Direction.DOWN
        '<' -> Direction.LEFT
        '>' -> Direction.RIGHT
        else -> throw IllegalArgumentException()
    } }

    val visited = mutableSetOf<Coord>()

    var currentLocation = Coord(0, 0)
    visited.add(currentLocation)

    commands.forEach { command ->
        currentLocation += command
        visited.add(currentLocation)
    }

    return visited.size
}

fun puzzle2(fileName: String): Int {
    val commands = File(fileName).readLines().first().map { when(it) {
        '^' -> Direction.UP
        'v' -> Direction.DOWN
        '<' -> Direction.LEFT
        '>' -> Direction.RIGHT
        else -> throw IllegalArgumentException()
    } }

    val visited = mutableSetOf<Coord>()

    var currentLocationSanta = Coord(0, 0)
    var currentLocationRobot = Coord(0, 0)
    visited.add(currentLocationSanta)

    commands.chunked(2).forEach { command ->

        currentLocationSanta += command.first()
        visited.add(currentLocationSanta)

        if (command.size == 2) {
            currentLocationRobot += command.last()
            visited.add(currentLocationRobot)

        }
    }

    return visited.size
}
