package twenty15.day09

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2015/day09-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2015/day09.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2015/day09-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2015/day09.txt") }
}

fun walkTree(city: String?, cities: Set<String>, distances:  Map<Pair<String, String>, Int>, selector: (Collection<Int>) -> Int): Int {
    if (cities.isEmpty()) return 0

    return selector( cities.map {
        (distances[city to it] ?: 0) + walkTree(it, cities.minus(it), distances, selector)
    } )
}

fun puzzle1(fileName: String): Int {
    val (cities, distances) = loadCitiesAndDistances(fileName)

    return walkTree(null, cities, distances) { it.min() }
}

fun puzzle2(fileName: String): Int {
    val (cities, distances) = loadCitiesAndDistances(fileName)

    return walkTree(null, cities, distances) { it.max() }
}

private fun loadCitiesAndDistances(fileName: String): Pair<Set<String>, Map<Pair<String, String>, Int>> {
    val lines = File(fileName).readLines()

    val cities =
        lines.flatMap { listOf(it.substringBefore(" to "), it.substringAfter(" to ").substringBefore(" = ")) }.toSet()

    val distances = lines.flatMap {
        val one = it.substringBefore(" to ")
        val two = it.substringAfter(" to ").substringBefore(" = ")
        val distance = it.substringAfter(" = ").toInt()

        listOf(
            (one to two) to distance,
            (two to one) to distance
        )
    }.toMap()

    return Pair(cities, distances)
}
