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
        currentLocation = when(command) {
            Direction.UP -> currentLocation + Coord(0, -1)
            Direction.DOWN -> currentLocation + Coord(0, 1)
            Direction.LEFT -> currentLocation + Coord(-1, 0)
            Direction.RIGHT -> currentLocation + Coord(1, 0)
        }
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

        currentLocationSanta = when(command.first()) {
            Direction.UP -> currentLocationSanta + Coord(0, -1)
            Direction.DOWN -> currentLocationSanta + Coord(0, 1)
            Direction.LEFT -> currentLocationSanta + Coord(-1, 0)
            Direction.RIGHT -> currentLocationSanta + Coord(1, 0)
        }
        visited.add(currentLocationSanta)

        if (command.size == 2) {
            currentLocationRobot = when(command.last()) {
                Direction.UP -> currentLocationRobot + Coord(0, -1)
                Direction.DOWN -> currentLocationRobot + Coord(0, 1)
                Direction.LEFT -> currentLocationRobot + Coord(-1, 0)
                Direction.RIGHT -> currentLocationRobot + Coord(1, 0)
            }
            visited.add(currentLocationRobot)

        }
    }

    return visited.size
}
