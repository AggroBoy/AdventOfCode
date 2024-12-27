package twenty24.day13

import util.*
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day13-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day13.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day13-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day13.txt") }
}

data class Machine(
    val buttonA: Coord,
    val buttonB: Coord,
    val prizeLocation: Coord
) {
    val buttonACost: Int
        get() = 3
    val buttonBCost: Int
        get() = 1
}

fun puzzle1(fileName: String): Long {
    val machines = loadMachines(fileName)

    return machines.mapNotNull { machine ->
        val maxA = minOf(100,machine.prizeLocation.x / machine.buttonA.x, machine.prizeLocation.y / machine.buttonA.y)
        val maxB = minOf(100,machine.prizeLocation.x / machine.buttonB.x, machine.prizeLocation.y / machine.buttonB.y)
        (0..maxA).flatMap { a ->
            (0..maxB).mapNotNull { b ->
                if (machine.buttonA * a + machine.buttonB * b == machine.prizeLocation) {
                    machine.buttonACost * a + machine.buttonBCost * b
                } else {
                    null
                }
            }
        }.minOrNull()
    }.sum()
}

fun puzzle2(fileName: String): Long {
    // Had to look up the solution - didn't have the maths to solve this one
    // I didn't even reccognise that I was looking at a simultaneous equation, and I'd
    // definitely never heard of Cramer's Rule
    val machines = loadMachines(fileName).map { it.copy(prizeLocation = it.prizeLocation + Coord(10000000000000L, 10000000000000L)) }

    return machines.mapNotNull { machine ->
        val det = machine.buttonA.x * machine.buttonB.y - machine.buttonA.y * machine.buttonB.x
        val a = (machine.prizeLocation.x * machine.buttonB.y - machine.prizeLocation.y * machine.buttonB.x) / det
        val b = (machine.buttonA.x * machine.prizeLocation.y - machine.buttonA.y * machine.prizeLocation.x) / det
        if (machine.buttonA.x * a + machine.buttonB.x * b == machine.prizeLocation.x && machine.buttonA.y * a + machine.buttonB.y * b == machine.prizeLocation.y) {
            machine.buttonACost * a + machine.buttonBCost * b
        } else {
            null
        }
    }.sum()
}

fun loadMachines(fileName: String): List<Machine> {
    return File(fileName).readLines().chunked(4).map {
        val buttonA = Regex("""Button A: X\+?([\d-]*), Y\+?([\d-]*)""").matchEntire(it[0])?.let { match ->
            Coord(match.groupValues[1].toInt(), match.groupValues[2].toInt())
        } ?: throw IllegalArgumentException("Invalid input")
        val buttonB = Regex("""Button B: X\+?([\d-]*), Y\+?([\d-]*)""").matchEntire(it[1])?.let { match ->
            Coord(match.groupValues[1].toInt(), match.groupValues[2].toInt())
        } ?: throw IllegalArgumentException("Invalid input")
        val prizeLocation = Regex("""Prize: X=([\d-]*), Y=([\d-]*)""").matchEntire(it[2])?.let { match ->
            Coord(match.groupValues[1].toInt(), match.groupValues[2].toInt())
        } ?: throw IllegalArgumentException("Invalid input")

        Machine(buttonA, buttonB, prizeLocation)
    }
}
