package twenty25.day07

import util.Cache
import util.Coord
import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2025/day07-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2025/day07.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day07-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day07.txt") }
}

fun puzzle1(fileName: String): Long {
    val lines = File(fileName).readLines()
    val start: Coord = Coord(lines.first().indexOf('S'), 0)
    val splitters = lines.flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            when (char) {
                '^' -> Coord(x, y)
                else -> null
            }
        }.filterNotNull()
    }
    val beam = mutableListOf<Coord>(start)
    var splits = 0L

    (1L..lines.size).forEach { y ->
        val incomingBeamsX = beam.filter { it.y == y-1L }.map { it.x }
        val (splitAtX, straightThroughX) = incomingBeamsX.partition { x -> splitters.contains(Coord(x,y)) }

        splits += splitAtX.size

        val newBeamsX = splitAtX.flatMap { x ->
            listOf(x-1L, x+1L)
        }.filterNot {straightThroughX.contains(it) }.toSet()

        beam.addAll(straightThroughX.map{Coord(it, y)} )
        beam.addAll(newBeamsX.map{Coord(it, y)} )
    }

    return splits
}

val timelineCache = Cache<Coord, Long>()
fun puzzle2(fileName: String): Long {
    val lines = File(fileName).readLines()
    val start: Coord = Coord(lines.first().indexOf('S'), 0)
    val splitters = lines.flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            when (char) {
                '^' -> Coord(x, y)
                else -> null
            }
        }.filterNotNull()
    }

    return walkTimelines(start, splitters)
}

fun walkTimelines(start: Coord, splitters: List<Coord>): Long {
    val nextSplitter = splitters.filter { it.x == start.x && it.y > start.y}.minByOrNull { it.y }
    if (nextSplitter == null) return 1L
    return listOf(nextSplitter + Coord(-1, 0), nextSplitter + Coord(1, 0))
        .sumOf{ timelineCache.getOrStore(it) { walkTimelines(it, splitters) } }
}
