package Day23

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day23-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day23.txt") }
//    printTimedOutput("Puzzle 2 test") { puzzle2("input/day23-test.txt") }
//    printTimedOutput("Puzzle 2     ") { puzzle2("input/day23.txt") }
}

fun puzzle1(fileName: String): Int {

    val connections = File(fileName).readLines().flatMap { connection ->
        val (one, two) = connection.split('-')
        listOf(one to two, two to one)
    }.groupBy { it.first }.mapValues { it.value.map { it.second } }

    return connections.flatMap { (key, value) ->
        value.flatMap { otherValue ->
            connections[otherValue]!!.intersect(value).map {
                setOf(key, otherValue, it)
            }
        }
    }
        .distinct()
        .filter{ it.any { it.startsWith("t") } }
        .count()
}

fun puzzle2(fileName: String): Int {
    return -1
}
