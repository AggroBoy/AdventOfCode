package twenty24.day18

import util.Coord
import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day18-test.txt", 7, 12) }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day18.txt", 71, 1024) }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day18-test.txt", 7) }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day18.txt", 71) }
}

fun puzzle1(fileName: String, memorySize: Int, corruptedBytes: Int): Int {
    val corruptedLocations = File(fileName).readLines().subList(0, corruptedBytes).map {
        Coord(it.substringBefore(",").toInt(), it.substringAfter(",").toInt())
    }.toHashSet()

    val visitedLocations: MutableMap<Coord, Int> = mutableMapOf()
    val targetLocation = Coord(memorySize - 1, memorySize - 1)

    fun walkMemory(location: Coord, length: Int = 0): Int? {
        if (location.x < 0 || location.y < 0 || location.x >= memorySize || location.y >= memorySize) return null
        if (corruptedLocations.contains(location)) return null
        if (visitedLocations.getOrDefault(location, Int.MAX_VALUE) <= length) return null

        if (location == targetLocation)
            return length

        visitedLocations[location] = length

        return listOf(
            Coord(1, 0),
            Coord(-1, 0),
            Coord(0, 1),
            Coord(0, -1)
        )
            .map { direction ->
                walkMemory(location + direction, length + 1)
            }
            .filterNotNull().minOrNull()
    }

    return walkMemory(Coord(0, 0)) ?: -1
}

fun puzzle2(fileName: String, memorySize: Int): String {
    val locationsThatWillCorrupt = File(fileName).readLines().map {
        Coord(it.substringBefore(",").toInt(), it.substringAfter(",").toInt())
    }

    val targetLocation = Coord(memorySize - 1, memorySize - 1)
    val visitedLocations:MutableSet<Coord> = mutableSetOf()

    fun canReach(location: Coord, corruptedLocations: HashSet<Coord>): Boolean {
        if (location.x < 0 || location.y < 0 || location.x >= memorySize || location.y >= memorySize) return false
        if (corruptedLocations.contains(location)) return false
        if (visitedLocations.contains(location)) return false

        if (location == targetLocation) return true

        visitedLocations.add(location)

        return listOf(
            Coord(1, 0),
            Coord(-1, 0),
            Coord(0, 1),
            Coord(0, -1)
        )
            .map { direction ->
                canReach(location + direction, corruptedLocations)
            }
            .any { it }
    }

    var corruptedBytes = 0
    while (canReach(Coord(0, 0), locationsThatWillCorrupt.subList(0, corruptedBytes).toHashSet())) {
        visitedLocations.clear()
        corruptedBytes++
    }

    return locationsThatWillCorrupt[corruptedBytes - 1].toString()
}
