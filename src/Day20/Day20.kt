package Day20

import util.Coord
import util.Direction
import util.printTimedOutput
import java.io.File
import kotlin.math.abs

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day20-test.txt", 1) }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day20.txt", 100) }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day20-test.txt", 50) }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day20.txt", 100) }
}

fun puzzle1(fileName: String, threshold: Int): Int = solvePuzzle(fileName, threshold, 2)
fun puzzle2(fileName: String, threshold: Int): Int = solvePuzzle(fileName, threshold, 20)


fun mapRouteTimings(walls: MutableSet<Coord>, startLocation: Coord, targetLocation: Coord): Map<Coord, Int> {
    val route: MutableMap<Coord, Int> = mutableMapOf()
    var currentLocation = startLocation
    var picoSeconds = 0
    while (true) {
        route[currentLocation] = picoSeconds++

        if (currentLocation == targetLocation) {
            return route
        }

        currentLocation = listOf(
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT
        ).map {
            currentLocation + it.coord
        }.filter {
            !walls.contains(it) && !route.contains(it)
        }.first()
    }
}

private fun solvePuzzle(fileName: String, threshold: Int, cheatDuration : Int): Int {
    var startLocation = Coord(0, 0)
    var targetLocation = Coord(0, 0)

    val lines = File(fileName).readLines()
    val walls: MutableSet<Coord> = mutableSetOf()

    lines.mapIndexed { y, line ->
        line.mapIndexed { x, char ->
            if (char == 'S') startLocation = Coord(x, y)
            if (char == 'E') targetLocation = Coord(x, y)
            if (char == '#') walls.add(Coord(x, y))
        }
    }

    val route = mapRouteTimings(walls, startLocation, targetLocation)

    val possibleCheatDeltas = getPossibleCheatsForDuration(cheatDuration)
    val possibleCheats: MutableMap<Pair<Coord, Coord>, Int> = mutableMapOf()

    route.map { cheatStart ->
        possibleCheatDeltas.map { cheat ->
            cheatStart.key + cheat to cheat.manhattanDistanceFrom(Coord(0, 0)).toInt()
        }.filter { (cheatEnd, _) ->
            route.contains(cheatEnd)
        }.map { (cheatEnd, cheatDuration) ->
            val saving = route[cheatEnd]!! - cheatStart.value - cheatDuration
            if (saving >= threshold) {
                possibleCheats[cheatStart.key to cheatEnd] = saving
            }
        }
    }

    return possibleCheats.count()
}

private fun getPossibleCheatsForDuration(cheatDuration: Int) =
    (-cheatDuration .. cheatDuration).flatMap { x ->
        (-cheatDuration .. cheatDuration).mapNotNull { y ->
            if (abs(x) + abs(y) <= cheatDuration) {
                Coord(x, y)
            } else {
                null
            }
        }
    }
