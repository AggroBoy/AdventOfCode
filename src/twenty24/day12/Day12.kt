package twenty24.day12

import util.*
import java.io.File

typealias GeoMap = List<List<Char>>
fun GeoMap.get(coord: Coord): Char = try { this[coord.y.toInt()][coord.x.toInt()] } catch (e: IndexOutOfBoundsException) { ' ' }

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day12-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day12.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day12-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day12.txt") }
}

fun puzzle1(fileName: String): Int {
    val counted: MutableSet<Coord> = mutableSetOf()

    val geoMap = loadMap(fileName)

    val areas = geoMap.indices.flatMap { y ->
        geoMap[0].indices.map { x ->
            getAreaAndBoundaryLength(geoMap, Coord(x, y), counted)
        }
    }.filter { it.first != 0 && it.second != 0 }

    return areas.map{ it.first * it.second}.sum()
}

fun getAreaAndBoundaryLength(geoMap: GeoMap, coord: Coord, counted: MutableSet<Coord>, targetLetter: Char? = null): Pair<Int, Int> {
    val letter = targetLetter ?: geoMap.get(coord)
    if (geoMap.get(coord) != letter) return 0 to 1

    if (counted.contains(coord)) return 0 to 0

    counted.add(coord)
    return listOf(
        1 to 0,
        getAreaAndBoundaryLength(geoMap, coord + Coord(1, 0), counted, letter),
        getAreaAndBoundaryLength(geoMap, coord + Coord(-1, 0), counted, letter),
        getAreaAndBoundaryLength(geoMap, coord + Coord(0, 1), counted, letter),
        getAreaAndBoundaryLength(geoMap, coord + Coord(0, -1), counted,  letter)
    ).reduce { acc, pair -> acc.first + pair.first to acc.second + pair.second }
}

fun puzzle2(fileName: String): Int {
    val counted: MutableSet<Coord> = mutableSetOf()

    val geoMap = loadMap(fileName)

    val regions = geoMap.indices.flatMap { y ->
        geoMap[0].indices.map { x ->
            getAreaAndBoundary(geoMap, Coord(x, y), counted)
        }
    }.filter { it.area != 0 }

    fun countSides(edges: List<Long>): Int {
        return edges.indices.map { index ->
            when {
                index == 0 -> 1
                edges[index] == edges[index - 1] + 1 -> 0
                else -> 1
            }
        }.sum()
    }

    val foo = regions.map { region ->
        val topBoundaries = region.topEdges.groupBy { it.y }.map { it.value.map { it.x }.sorted() }
        val topSides = topBoundaries.map { countSides(it) }.sum()
        val bottomBoundaries = region.bottomEdges.groupBy { it.y }.map { it.value.map { it.x }.sorted() }
        val bottomSides = bottomBoundaries.map { countSides(it) }.sum()
        val leftBoundaries = region.leftEdges.groupBy { it.x }.map { it.value.map { it.y }.sorted() }
        val leftSides = leftBoundaries.map { countSides(it) }.sum()
        val rightBoundaries = region.rightEdges.groupBy { it.x }.map { it.value.map { it.y }.sorted() }
        val rightSides = rightBoundaries.map { countSides(it) }.sum()

        region.area to (topSides + bottomSides + leftSides + rightSides)
    }

    return foo.map{ it.first * it.second}.sum()
}

data class Region(
    val area: Int,
    val topEdges: Set<Coord> = emptySet(),
    val leftEdges: Set<Coord> = emptySet(),
    val bottomEdges: Set<Coord> = emptySet(),
    val rightEdges: Set<Coord> = emptySet()
) {
    operator fun plus(region: Region): Region {
        return Region(area + region.area,
            topEdges + region.topEdges,
            leftEdges + region.leftEdges,
            bottomEdges + region.bottomEdges,
            rightEdges + region.rightEdges)
    }
}

fun getAreaAndBoundary(geoMap: GeoMap, coord: Coord, counted: MutableSet<Coord>, targetLetter: Char? = null): Region {
    val letter = targetLetter ?: geoMap.get(coord)
    //if (geoMap.get(coord) != letter) return 0 to setOf(coord)

    if (counted.contains(coord)) return Region(0)
    counted.add(coord)

    var region = Region(1)

    region += if (geoMap.get(coord + Coord(1, 0)) != letter) {
        Region(0, rightEdges = setOf(coord + Coord(1, 0)))
    } else {
        getAreaAndBoundary(geoMap, coord + Coord(1, 0), counted, letter)
    }

    region += if (geoMap.get(coord + Coord(-1, 0)) != letter) {
        Region(0, leftEdges = setOf(coord + Coord(-1, 0)))
    } else {
        getAreaAndBoundary(geoMap, coord + Coord(-1, 0), counted, letter)
    }

    region += if (geoMap.get(coord + Coord(0, 1)) != letter) {
        Region(0, bottomEdges = setOf(coord + Coord(0, 1)))
    } else {
        getAreaAndBoundary(geoMap, coord + Coord(0, 1), counted, letter)
    }

    region += if (geoMap.get(coord + Coord(0, -1)) != letter) {
        Region(0, topEdges = setOf(coord + Coord(0, -1)))
    } else {
        getAreaAndBoundary(geoMap, coord + Coord(0, -1), counted, letter)
    }

    return region
}

fun loadMap(fileName: String): GeoMap {
    return File(fileName).readLines().map { it.toList() }
}
