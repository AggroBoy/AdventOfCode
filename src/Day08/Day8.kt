package Day08

import util.printTimedOutput
import java.io.File

typealias Coord = Pair<Int, Int>
val Coord.x get() = this.first
val Coord.y get() = this.second

data class TownMap (val width: Int, val height: Int, val antenna: Map<Char, List<Coord>>)

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day8-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day8.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day8-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day8.txt") }
}

fun puzzle1(fileName: String): Int {
    val townMap = loadMap(fileName)

    return townMap.antenna.flatMap { (c, coords) ->
        coords.flatMap { coord ->
            coords
                .filter { it != coord }
                .map { otherCoord ->
                    val xDiff = coord.x - otherCoord.x
                    val yDiff = coord.y - otherCoord.y
                    Coord(coord.x + xDiff, coord.y + yDiff)
                }
                .filter { antinode ->
                    antinode.x >= 0 && antinode.x < townMap.width &&
                            antinode.y >= 0 && antinode.y < townMap.height
                }
        }
    }.toSet().count()
}

fun puzzle2(fileName: String): Int {
    return -1
}

fun loadMap(fileName: String): TownMap {
    val lines = File(fileName).readLines()
    val width = lines.first().length
    val height = lines.size
    val antenna: MutableMap<Char, List<Coord>> = mutableMapOf()

    lines.indices.forEach { y ->
        lines[y].indices.forEach { x ->
            val c = lines[y][x]
            if (c != '.') {
                val coord = Coord(x, y)
                val list = antenna.getOrDefault(c, listOf())
                antenna[c] = list + coord
            }
        }
    }

    return TownMap(width, height, antenna)
}