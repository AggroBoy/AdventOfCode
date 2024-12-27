package twenty24.day06

import util.printTimedOutput
import java.io.File

typealias Map = List<List<Boolean>>

data class Guard(
    var x: Int = 0,
    var y: Int = 0,
    var direction: Char = '^'
) {
    fun turnRight() {
        direction = when (direction) {
            '^' -> '>'
            '>' -> 'v'
            'v' -> '<'
            '<' -> '^'
            else -> throw IllegalArgumentException("Invalid direction: $direction")
        }
    }
}

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day6-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day6.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day6-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day6.txt") }
}

fun puzzle1(fileName: String): Int {
    val (map, guard) = loadMap(fileName)

    return getGuardVisitedLocations(guard, map).size
}

private fun getGuardVisitedLocations(
    guard: Guard,
    map: Map
): MutableSet<Pair<Int, Int>> {
    val visitedPositions: MutableSet<Pair<Int, Int>> = mutableSetOf()

    while (0 < guard.x && guard.x < map[0].size && 0 < guard.y && guard.y < map.size) {
        visitedPositions.add(guard.x to guard.y)
        when {
            guard.direction == '^' && !map.blocked(guard.x, guard.y - 1) -> guard.y--
            guard.direction == '>' && !map.blocked(guard.x + 1, guard.y) -> guard.x++
            guard.direction == 'v' && !map.blocked(guard.x, guard.y + 1) -> guard.y++
            guard.direction == '<' && !map.blocked(guard.x - 1, guard.y) -> guard.x--
            else -> guard.turnRight()
        }
    }
    return visitedPositions
}

fun puzzle2(fileName: String): Int {
    val (map, guard) = loadMap(fileName)

    val visitedLocations = getGuardVisitedLocations(guard.copy(), map)

    return visitedLocations.map { addingBlockCausesLoop(map, guard.copy(), it.first to it.second) }.count { it == true}
}

fun addingBlockCausesLoop(map: Map, guard: Guard, location: Pair<Int, Int>): Boolean {
    fun canGo(x: Int, y: Int): Boolean {
        return (!map.blocked(x, y)) && (location != x to y)
    }
    val guardHistory: MutableSet<Guard> = mutableSetOf()

    while (0 < guard.x && guard.x < map[0].size && 0 < guard.y && guard.y < map.size && !guardHistory.contains(guard)) {
        guardHistory.add(guard.copy())
        when {
            guard.direction == '^' && canGo(guard.x, guard.y - 1) -> guard.y--
            guard.direction == '>' && canGo(guard.x + 1, guard.y) -> guard.x++
            guard.direction == 'v' && canGo(guard.x, guard.y + 1) -> guard.y++
            guard.direction == '<' && canGo(guard.x - 1, guard.y) -> guard.x--
            else -> guard.turnRight()
        }
    }

    return guardHistory.contains(guard)
}

fun Map.blocked(x: Int, y: Int): Boolean {
    try {
        return this[y][x]
    } catch (e: IndexOutOfBoundsException) {
        // Out of bounds is not blocked; the guard can leave the map
        return false
    }
}

fun loadMap(fileName: String): Pair<Map, Guard> {
    var guard = Guard()

    val map: Map = File(fileName).readLines().mapIndexed { y, line ->
        line.mapIndexed { x, char ->
            if (char in listOf('^', 'v', '<', '>')) {
                guard = Guard(x, y, char)
            }
            char == '#'
        }
    }

    return map to guard
}
