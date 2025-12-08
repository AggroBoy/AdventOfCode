package twenty25.day08

import util.Coord3d
import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2025/day08-test.txt", 10) }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2025/day08.txt", 1000) }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day08-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day08.txt") }
}

class Playground(fileName: String) {
    val boxes: List<Coord3d> = File(fileName)
        .readLines()
        .map { it.split(",") }
        .map { Coord3d(it[0].toLong(), it[1].toLong(), it[2].toLong()) }
    val pairDistances = boxes.flatMap { one ->
        boxes.filter { it != one }.map { one to it }
    }.associateBy { it.first.disatanceFrom(it.second) }
    val sortedDistances = pairDistances.keys.sorted()
    val circuits = boxes.map { mutableListOf(it) }.toMutableList()
}

fun puzzle1(fileName: String, iterations: Int): Long {
    val playground = Playground(fileName)

    with(playground) {
        sortedDistances.subList(0, iterations).forEach { distance ->
            val boxOne = pairDistances[distance]?.first ?: throw IllegalArgumentException("Nope")
            val circuitOne = circuits.find { it.contains(boxOne) } ?: throw IllegalArgumentException("Nope")
            val boxTwo = pairDistances[distance]?.second ?: throw IllegalArgumentException("Nope")
            val circuitTwo = circuits.find { it.contains(boxTwo) } ?: throw IllegalArgumentException("Nope")

            if (circuitOne != circuitTwo) {
                circuitOne.addAll(circuitTwo)
                circuits.remove(circuitTwo)
            }
        }

        return circuits.map { it.size }.sortedDescending().subList(0, 3).reduce { acc, size -> acc * size }.toLong()
    }
}

fun puzzle2(fileName: String): Long {
    val playground = Playground(fileName)

    with (playground) {
        sortedDistances.forEach { distance ->
            val boxOne = pairDistances[distance]?.first ?: throw IllegalArgumentException("Nope")
            val circuitOne = circuits.find { it.contains(boxOne) } ?: throw IllegalArgumentException("Nope")
            val boxTwo = pairDistances[distance]?.second ?: throw IllegalArgumentException("Nope")
            val circuitTwo = circuits.find { it.contains(boxTwo) } ?: throw IllegalArgumentException("Nope")

            if (circuitOne != circuitTwo) {
                if (circuits.size == 2) {
                    return (boxOne.x * boxTwo.x)
                }
                circuitOne.addAll(circuitTwo)
                circuits.remove(circuitTwo)
            }
        }
        throw Exception("Ran out!")
    }
}
