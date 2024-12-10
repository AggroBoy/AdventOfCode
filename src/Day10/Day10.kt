package Day10

import util.*
import java.io.File

typealias TopoMap = List<List<Int>>
fun TopoMap.get(coord: Coord): Int = this.get(coord.x, coord.y)
fun TopoMap.get(x: Int, y: Int): Int {
    try {
        return this[y][x]
    } catch (e: IndexOutOfBoundsException) {
        // doesn't need to be an error; just make sure it doesn't route
        return -1
    }
}

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day10-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day10.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day10-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day10.txt") }
}

fun puzzle1(fileName: String): Int {
    val topoMap: TopoMap = loadTopoMap(fileName)

    return findAndScoreTrailheads(topoMap) { map, coord -> getReachablePeaksForTrailHead(map, coord).count() }
}

fun puzzle2(fileName: String): Int {
    val topoMap: TopoMap = loadTopoMap(fileName)

    return findAndScoreTrailheads(topoMap) { map, coord -> getTrailHeadRating(map, coord) }
}

private fun findAndScoreTrailheads(map: TopoMap, trailheadScore: (TopoMap, Coord) -> Int) = map.indices.flatMapIndexed { y, _ ->
    map[y].indices.mapIndexed { x, _ ->
        if (map.get(x, y) == 0) {
            trailheadScore(map, Coord(x, y))
        } else
            0
    }
}.sum()

fun getReachablePeaksForTrailHead(map: TopoMap, currentPosition: Coord): Set<Coord> {
    val height = map.get(currentPosition)

    if (height == 9) {
        return setOf(currentPosition)
    }

    return listOf(Coord(-1, 0), Coord(1, 0), Coord(0, -1), Coord(0, 1) ).flatMap {
        if (map.get(currentPosition + it) == height + 1) {
            getReachablePeaksForTrailHead(map, currentPosition + it)
        } else {
            emptySet()
        }
    }.toSet()
}

fun getTrailHeadRating(map: TopoMap, currentPosition: Coord): Int {
    val height = map.get(currentPosition)

    if (height == 9) {
        return 1
    }

    return listOf(Coord(-1, 0), Coord(1, 0), Coord(0, -1), Coord(0, 1) ).map {
        if (map.get(currentPosition + it) == height + 1) {
            getTrailHeadRating(map, currentPosition + it)
        } else {
            0
        }
    }.sum()
}

private fun loadTopoMap(fileName: String) = File(fileName).readLines().map { it.map { it.digitToInt() } }
