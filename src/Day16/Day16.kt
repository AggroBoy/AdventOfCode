package Day16

import util.Direction
import util.Coord
import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day16-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day16.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day16-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day16.txt") }
}

data class Reindeer(val location: Coord, val facing: Direction)
enum class LocationType { WALL, OPEN, START, END}

var bestScoreSoFar = Long.MAX_VALUE
var bestScoreForReindeerState: MutableMap<Reindeer, Long> = mutableMapOf()

typealias Maze = Array<Array<LocationType>>
fun Maze.get(location: Coord) = this[location.y.toInt()][location.x.toInt()]


fun puzzle1(fileName: String): Long {
    val (maze, startPosition) = loadMazeAndStartPosition(fileName)
    val reindeer = Reindeer(startPosition, Direction.RIGHT)

    bestScoreSoFar = Long.MAX_VALUE
    bestScoreForReindeerState = mutableMapOf()
    return walkMaze(maze, reindeer)?.first ?: -1
}

fun puzzle2(fileName: String): Int {
    val (maze, startPosition) = loadMazeAndStartPosition(fileName)
    val reindeer = Reindeer(startPosition, Direction.RIGHT)

    bestScoreSoFar = Long.MAX_VALUE
    bestScoreForReindeerState = mutableMapOf()
    val uniqueLocations = walkMaze(maze, reindeer)?.second?.toSet() ?: return -1
    return uniqueLocations.size
}

fun walkMaze(
    maze: Maze,
    reindeer: Reindeer,
    visitedLocations: List<Reindeer> = emptyList(),
    score: Long = 0L
): Pair<Long, List<Coord>>? {
    if (score > bestScoreSoFar || score > (bestScoreForReindeerState[reindeer] ?: Long.MAX_VALUE)) {
        return null
    } else {
        bestScoreForReindeerState[reindeer] = score
    }

    if (maze[reindeer.location.y.toInt()][reindeer.location.x.toInt()] == LocationType.END) {
        bestScoreSoFar = score
        return score to visitedLocations.map { it.location } + reindeer.location
    }

    val potentials = listOf(
        1 to reindeer.copy(location = reindeer.location + reindeer.facing.coord),
        1001 to reindeer.copy(location = reindeer.location + reindeer.facing.turnRight().coord, facing = reindeer.facing.turnRight()),
        1001 to reindeer.copy(location = reindeer.location + reindeer.facing.turnLeft().coord, facing = reindeer.facing.turnLeft())
    ).filter { (_, newReindeer) ->
        visitedLocations.none{ it.location == newReindeer.location} && maze.get(newReindeer.location) != LocationType.WALL
    }

    if (potentials.isEmpty()) {
        return null
    }

    val results = potentials.map { (addScore, newReindeer) ->
        walkMaze(maze, newReindeer, visitedLocations + reindeer, score + addScore)
    }.filterNotNull()
    val minScore = results.minByOrNull { it.first }?.first ?: return null

    return minScore to results.filter { it.first == minScore }.map { it.second }.reduce { acc, locations -> acc + locations }
}

fun loadMazeAndStartPosition(fileName: String): Pair<Maze, Coord> {
    val lines = File(fileName).readLines()

    val tempMaze = Maze(lines.size) { Array(lines[0].length) { LocationType.OPEN } }
    var startPosition = Coord(0, 0)

    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            when (char) {
                '#' -> tempMaze[y][x] = LocationType.WALL
                'S' -> startPosition = Coord(x, y)
                'E' -> tempMaze[y][x] = LocationType.END
            }
        }
    }

    return tempMaze to startPosition
}
