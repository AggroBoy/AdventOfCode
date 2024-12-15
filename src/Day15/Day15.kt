package Day15

import util.*
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day15-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day15.txt") }
//    printTimedOutput("Puzzle 2 test") { puzzle2("input/day15-test.txt") }
//    printTimedOutput("Puzzle 2     ") { puzzle2("input/day15.txt") }
}


enum class Direction(val coord: Coord) {
    UP(Coord(0, -1)),
    DOWN(Coord(0, 1)),
    LEFT(Coord(-1, 0)),
    RIGHT(Coord(1, 0))
}
data class Box(var location: Coord) {
    val gps get() = location.x + (100 * location.y)
}

class Warehouse(lines: List<String>) {
    val size: Coord
    val walls: List<Coord>
    val boxes: List<Box>
    var robotLocation: Coord = Coord(0, 0)

    init {
        size = Coord(lines[0].length, lines.size)
        val tempWalls = mutableListOf<Coord>()
        val tempBoxes = mutableListOf<Box>()

        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, item ->
                when (item) {
                    '#' -> tempWalls.add(Coord(x, y))
                    '@' -> robotLocation = Coord(x, y)
                    'O' -> tempBoxes.add(Box(Coord(x, y)))
                }
            }
        }

        walls = tempWalls.toList()
        boxes = tempBoxes.toList()
    }

    fun moveRobot(direction: Direction) {
        val newLocation = robotLocation + direction.coord

        if (pushInto(newLocation, direction)) {
            robotLocation = newLocation
        }
    }

    fun pushInto(location: Coord, direction: Direction): Boolean {
        if (walls.contains(location)) return false
        val box = boxes.firstOrNull() { it.location == location } ?: return true

        val newBoxLocation = box.location + direction.coord
        if (pushInto(newBoxLocation, direction)) {
            box.location = newBoxLocation
            return true
        }

        return false
    }

    fun getTotalGPS(): Long {
        return boxes.map { it.gps }.sum()
    }

    fun getRenbderedWareHouse(): String {
        val renderedWarehouse = StringBuilder()
        for (y in 0 until size.y) {
            for (x in 0 until size.x) {
                val coord = Coord(x, y)
                when {
                    walls.contains(coord) -> renderedWarehouse.append('#')
                    robotLocation == coord -> renderedWarehouse.append('@')
                    boxes.any { it.location == coord } -> renderedWarehouse.append('O')
                    else -> renderedWarehouse.append('.')
                }
            }
            renderedWarehouse.append("\n")
        }
        return renderedWarehouse.toString()
    }
}

fun puzzle1(fileName: String): Long {
    val lines = File(fileName).readLines()
    val blankLine = lines.indexOfFirst { it.isBlank() }
    val warehouse = Warehouse(lines.subList(0, blankLine))
    val instructions = lines.subList(blankLine + 1, lines.size).flatMap{
        it.map { it}
    }

    instructions.forEach {
        when (it) {
            '^' -> warehouse.moveRobot(Direction.UP)
            'v' -> warehouse.moveRobot(Direction.DOWN)
            '<' -> warehouse.moveRobot(Direction.LEFT)
            '>' -> warehouse.moveRobot(Direction.RIGHT)
        }
    }
    //println(warehouse.getRenbderedWareHouse())
    return warehouse.getTotalGPS()
}

fun puzzle2(fileName: String): Long {
    return -1
}
