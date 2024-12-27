package twenty24.day08

import util.*
import java.io.File

data class TownMap (val width: Int, val height: Int, val antenna: Map<Char, List<Coord>>)

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2024/day8-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2024/day8.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2024/day8-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2024/day8.txt") }
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
                .filter { antinode -> townMap.pointWithinBounds(antinode) }
        }
    }.toSet().count()
}

fun puzzle2(fileName: String): Int {
    val townMap = loadMap(fileName)

    // Get the pairs of antenna to generate antinodes for
    val uniqueAntennaPairs = getUniqueAntennaPairs(townMap)

    // Generate the antinodes for each pair of antennas
    return uniqueAntennaPairs
        .flatMap { (coord1, coord2) ->
            // figure out the vector the antinodes will be generated along
            val vector = Coord(coord1.x - coord2.x, coord1.y - coord2.y)

            val antinodes: MutableList<Coord> = mutableListOf()

            // Walk along the vector in one direction, generating antinodes
            var pos = coord1.copy()
            while (townMap.pointWithinBounds(pos)) {
                antinodes.add(pos.copy())
                pos -= vector
            }

            // Walk along the vector in the other direction, generating antinodes
            pos = coord1.copy() + vector
            while (townMap.pointWithinBounds(pos)) {
                antinodes.add(pos.copy())
                pos += vector
            }

            antinodes
        }.toSet().size
}

// Figure out all the pairs of antennas that will generate resonant frequencies (ie, each unique pair of antennas that
// share a frequency)
private fun getUniqueAntennaPairs(townMap: TownMap): Set<Pair<Coord, Coord>> {
    return townMap.antenna.flatMap { (c, coords) ->
        coords.flatMap { coord ->
            coords.filter { it != coord }.map { otherCoord ->
                // Have to sort the pair of antennas otherwise we'll get duplicates due to the order of the antennas
                // in the pair
                if (coord.x < otherCoord.x || coord.x == otherCoord.x && coord.y < otherCoord.y)
                    Pair(coord, otherCoord)
                else
                    Pair(otherCoord, coord)
            }
        }
    }.toSet()
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

fun TownMap.pointWithinBounds(coord: Coord): Boolean =
    coord.x >= 0 && coord.x < width && coord.y >= 0 && coord.y < height
