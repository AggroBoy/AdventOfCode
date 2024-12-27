package twenty24.day15

import util.*
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day15-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day15.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day15-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day15.txt") }
}


data class Box(var locations: List<Coord>) {
    val gps get() = locations.map{ it.x }.min() + (100 * locations.map{ it.y }.min())
    fun renderLocation(location: Coord): Char {
        if (locations.size == 1) return 'O'
        return when (location.x) {
            locations.map { it.x }.min() -> '['
            locations.map { it.x }.max() -> ']'
            else -> 'O'
        }
    }
}

class Warehouse(lines: List<String>, val itemWidth: Int = 1) {
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
                val realX = x * itemWidth
                when (item) {
                    '#' -> (0 until itemWidth).forEach{ tempWalls.add(Coord(realX + it, y)) }
                    '@' -> robotLocation = Coord(realX, y)
                    'O' -> tempBoxes.add(Box((0 until itemWidth).map { Coord(realX+it, y) }))
                }
            }
        }

        walls = tempWalls.toList()
        boxes = tempBoxes.toList()
    }

    fun moveRobot(direction: Direction) {
        val newLocation = robotLocation + direction.coord

        if (canPushInto(newLocation, direction)) {
            doPushInto(newLocation, direction)
            robotLocation = newLocation
        }
    }

    fun canPushInto(location: Coord, direction: Direction, pushingBox: Box? = null): Boolean {
        if (walls.contains(location)) return false
        val box = boxes.firstOrNull() { it.locations.contains(location) && it != pushingBox } ?: return true

        return (box.locations.all { canPushInto(it + direction.coord, direction, box) })
    }

    fun doPushInto(location: Coord, direction: Direction, pushingBox: Box? = null) {
        val box = boxes.firstOrNull { it.locations.contains(location) && it != pushingBox} ?: return
        box.locations = box.locations.map {
            val newLocation = it + direction.coord
            doPushInto(newLocation, direction, box)
            newLocation
        }
    }

    fun getTotalGPS(): Long {
        return boxes.map { it.gps }.sum()
    }

    fun getRenderedWareHouse(): String {
        val renderedWarehouse = StringBuilder()
        for (y in 0 until size.y) {
            for (x in 0 until size.x * itemWidth) {
                val coord = Coord(x, y)
                when {
                    walls.contains(coord) -> renderedWarehouse.append('#')
                    robotLocation == coord -> renderedWarehouse.append('@')
                    boxes.any { it.locations.contains(coord) } -> {
                        renderedWarehouse.append(boxes.first { it.locations.contains(coord) }.renderLocation(coord))
                    }
                    else -> renderedWarehouse.append('.')
                }
            }
            renderedWarehouse.append("\n")
        }
        return renderedWarehouse.toString()
    }
}

fun puzzle1(fileName: String): Long {
    val (warehouse, instructions) = loadWarehouseAndInstructions(fileName, 1)

    processInstructions(instructions, warehouse)

    //println(warehouse.getRenderedWareHouse())
    return warehouse.getTotalGPS()
}

fun puzzle2(fileName: String): Long {
    val (warehouse, instructions) = loadWarehouseAndInstructions(fileName, 2)

    processInstructions(instructions, warehouse)

    //println(warehouse.getRenderedWareHouse())
    return warehouse.getTotalGPS()
}

private fun processInstructions(instructions: List<Char>, warehouse: Warehouse) {
    instructions.forEach {
        when (it) {
            '^' -> warehouse.moveRobot(Direction.UP)
            'v' -> warehouse.moveRobot(Direction.DOWN)
            '<' -> warehouse.moveRobot(Direction.LEFT)
            '>' -> warehouse.moveRobot(Direction.RIGHT)
        }
    }
}

private fun loadWarehouseAndInstructions(fileName: String, itemWidth: Int): Pair<Warehouse, List<Char>> {
    val lines = File(fileName).readLines()
    val blankLine = lines.indexOfFirst { it.isBlank() }
    val warehouse = Warehouse(lines.subList(0, blankLine), itemWidth)
    val instructions = lines.subList(blankLine + 1, lines.size).flatMap {
        it.map { it }
    }
    return Pair(warehouse, instructions)
}
