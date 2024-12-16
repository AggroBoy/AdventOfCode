package Day16

import util.Direction
import util.Coord
import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day16-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day16.txt") }
//    printTimedOutput("Puzzle 2 test") { puzzle2("input/day16-test.txt") }
//    printTimedOutput("Puzzle 2     ") { puzzle2("input/day16.txt") }
}

data class Reindeer(val location: Coord, val facing: Direction)
enum class LocationType { WALL, OPEN, START, END}

var bestScoreSoFar = Long.MAX_VALUE
var bestScoreForSquare: Array<Array<Long>> = emptyArray()

fun puzzle1(fileName: String): Long {
    val (maze, startPosition) = loadMazeAndStartPosition(fileName)
    val reindeer = Reindeer(startPosition, Direction.RIGHT)

    bestScoreSoFar = Long.MAX_VALUE
    bestScoreForSquare = Array(maze.size) { Array(maze[0].size) { Long.MAX_VALUE } }
    return walkMaze(maze, reindeer) ?: -1
}


fun walkMaze(
    maze: Array<Array<LocationType>>,
    reindeer: Reindeer,
    visitedLocations: List<Reindeer> = emptyList(),
    score: Long = 0L
): Long? {
    if (score >= bestScoreSoFar || score >= bestScoreForSquare[reindeer.location.y.toInt()][reindeer.location.x.toInt()]) {
        return null
    } else {
        bestScoreForSquare[reindeer.location.y.toInt()][reindeer.location.x.toInt()] = score
    }

    if (maze[reindeer.location.y.toInt()][reindeer.location.x.toInt()] == LocationType.END) {
        return score.also { bestScoreSoFar = it }
    }

    val potentials = listOf(
        1 to reindeer.copy(location = reindeer.location + reindeer.facing.coord),
        1001 to reindeer.copy(location = reindeer.location + reindeer.facing.turnRight().coord, facing = reindeer.facing.turnRight()),
        1001 to reindeer.copy(location = reindeer.location + reindeer.facing.turnLeft().coord, facing = reindeer.facing.turnLeft())
    ).filter { (_, newReindeer) ->
        visitedLocations.none{ it.location == newReindeer.location}
                && maze[newReindeer.location.y.toInt()][newReindeer.location.x.toInt()] != LocationType.WALL
    }

    if (potentials.isEmpty()) {
        return null
    }

    return potentials.map { (addScore, newReindeer) ->
        walkMaze(maze, newReindeer, visitedLocations + reindeer, score + addScore)
    }.filterNotNull().minOrNull()
}

fun loadMazeAndStartPosition(fileName: String): Pair<Array<Array<LocationType>>, Coord> {
    val lines = File(fileName).readLines()

    val tempMaze = Array(lines.size) { Array(lines[0].length) { LocationType.OPEN } }
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

fun puzzle2(fileNamne: String): Int {
    return -1
}
