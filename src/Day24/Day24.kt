package Day24

import util.printTimedOutput
import java.io.File
import javax.management.monitor.StringMonitor
import kotlin.math.sign

fun main() {
    printTimedOutput("Puzzle 1 test 1") { puzzle1("input/day24-test1.txt") }
    printTimedOutput("Puzzle 1 test 2") { puzzle1("input/day24-test2.txt") }
    printTimedOutput("Puzzle 1       ") { puzzle1("input/day24.txt") }
    printTimedOutput("Puzzle 2 test  ") { puzzle2("input/day24-test.txt2") }
    printTimedOutput("Puzzle 2       ") { puzzle2("input/day24.txt") }
}

data class Gate(
    val inputOne: String,
    val intputTwo: String,
    val operation: String,
    val output: String,
)

fun puzzle1(fileName: String): Long {
    val lines = File(fileName).readLines()
    val separator = lines.indexOf("")
    val initialState = lines.subList(0, separator)
    val gates = lines.subList(separator + 1, lines.size)

    val wireStates: MutableMap<String, Int> = mutableMapOf()
    initialState.forEach {
        val (wire, value) = it.split(": ")
        wireStates[wire] = value.toInt()
    }

    val program = gates.flatMap { gate ->
        val matches = """(...) ([A-Z]+) (...) -> (...)""".toRegex().find(gate) ?: error("Invalid gate: $gate")
        val gate = Gate(matches.groupValues[1], matches.groupValues[3], matches.groupValues[2], matches.groupValues[4])
        listOf(gate.output to gate)
    }.associateBy { it.first }.mapValues { it.value.second }

    fun getWireValue(wire: String): Int {
        val currentValue = wireStates[wire]
        if (currentValue != null) {
            return currentValue
        }

        val gate = program[wire] ?: error("No gate found for $wire")
        val calculatedValue = when (gate.operation) {
            "AND" -> getWireValue(gate.inputOne) and getWireValue(gate.intputTwo)
            "OR" -> getWireValue(gate.inputOne) or getWireValue(gate.intputTwo)
            "XOR" -> getWireValue(gate.inputOne) xor getWireValue(gate.intputTwo)
            else -> error("Invalid operation: ${gate.operation}")
        }
        wireStates[wire] = calculatedValue
        return calculatedValue
    }

    val outputs = program.values.map { it.output }.filter { it.startsWith("z") }
    return outputs.map {
        it to getWireValue(it)
    }.sortedByDescending { it.first }.map{ it.second }.joinToString("").toLong(2)
}

fun puzzle2(fileName: String): Int {
    return -1
}
