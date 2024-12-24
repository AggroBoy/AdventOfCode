package Day24

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test 1") { puzzle1("input/day24-test1.txt") }
    printTimedOutput("Puzzle 1 test 2") { puzzle1("input/day24-test2.txt") }
    printTimedOutput("Puzzle 1       ") { puzzle1("input/day24.txt") }
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

fun puzzle2(fileName: String): String {
    val lines = File(fileName).readLines()
    val separator = lines.indexOf("")
    val gates = lines.subList(separator + 1, lines.size)

    val program = gates.flatMap { gate ->
        val matches = """(...) ([A-Z]+) (...) -> (...)""".toRegex().find(gate) ?: error("Invalid gate: $gate")
        val gate = Gate(matches.groupValues[1], matches.groupValues[3], matches.groupValues[2], matches.groupValues[4])
        listOf(gate.output to gate)
    }.associateBy { it.first }.mapValues { it.value.second }


    // NGL; I picked through the program by hand and debugged it to get
    // these swaps. I'm sure I coule write a program to figuure it out.
    // (the swaps are all pretty proximate to the x, y or z gate for the
    // bad bit they fix. So identify lowest bad bit, get nearby gates and
    // use the permutation code I wrote to brute force the fix, and repeat
    // until no bad bits remain)
    val newProgram = program.toMutableMap()
    newProgram["z07"] = program["gmt"]!!.copy(output = "z07")
    newProgram["gmt"] = program["z07"]!!.copy(output = "gmt")

    newProgram["qjj"] = program["cbj"]!!.copy(output = "qjj")
    newProgram["cbj"] = program["qjj"]!!.copy(output = "cbj")

    newProgram["z18"] = program["dmn"]!!.copy(output = "z18")
    newProgram["dmn"] = program["z18"]!!.copy(output = "dmn")

    newProgram["z35"] = program["cfk"]!!.copy(output = "z35")
    newProgram["cfk"] = program["z35"]!!.copy(output = "cfk")

    val inputX = "101010101010101010101010101010101010101010101".toLong(2)
    val inputY = "010101010101010101010101010101010101010101010".toLong(2)
    val result = runWithInputs(newProgram, inputX, inputY)

    return listOf("z07", "gmt", "qjj", "cbj", "z18", "dmn", "z35", "cfk").sorted().joinToString(",")
}

fun generatePermuations(potentialGates: List<Gate>): List<List<Set<Gate>>> {
    return potentialGates.flatMap { oneOne ->
        potentialGates.filter { it != oneOne }.flatMap { oneTwo ->
            potentialGates.filter { it !in listOf(oneOne, oneTwo) }.flatMap { twoOne ->
                potentialGates.filter { it !in listOf(oneOne, oneTwo, twoOne) }.flatMap { twoTwo ->
                    potentialGates.filter { it !in listOf(oneOne, oneTwo, twoOne, twoTwo) }.flatMap { threeOne ->
                        potentialGates.filter { it !in listOf(oneOne, oneTwo, twoOne, twoTwo, threeOne) }.flatMap { threeTwo ->
                            potentialGates.filter { it !in listOf(oneOne, oneTwo, twoOne, twoTwo, threeOne, threeTwo) }.flatMap { fourOne ->
                                potentialGates.filter { it !in listOf(oneOne, oneTwo, twoOne, twoTwo, threeOne, threeTwo, fourOne) }.map { fourTwo ->
                                    listOf(
                                        setOf(oneOne, oneTwo),
                                        setOf(twoOne, twoTwo),
                                        setOf(threeOne, threeTwo),
                                        setOf(fourOne, fourTwo)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }.distinct()
}

fun getContributingGates(program: Map<String, Gate>, gate: Gate): List<Gate> {
    return listOf(program[gate.inputOne], program[gate.intputTwo]).filterNotNull().flatMap { listOf( it ) + getContributingGates(program, it) }
}

fun runWithInputs(program: Map<String, Gate>, inputX: Long, inputY: Long): Long {
    val wireStates: MutableMap<String, Int> = mutableMapOf()

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

    inputX.toBinaryString().reversed().forEachIndexed { index, value ->
        val wire = "x%02d".format(index)
        wireStates[wire] = value.toString().toInt()
    }
    inputY.toBinaryString().reversed().forEachIndexed { index, value ->
        val wire = "y%02d".format(index)
        wireStates[wire] = value.toString().toInt()
    }

    val outputs = program.values.map { it.output }.filter { it.startsWith("z") }
    val result = outputs.map {
        it to getWireValue(it)
    }.sortedByDescending { it.first }.map{ it.second }.joinToString("").toLong(2)

    return result
}

fun Long.toBinaryString(): String {
    return this.toString(2).padStart(45, '0')
}