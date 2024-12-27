package twenty24.day17

import util.printTimedOutput
import java.io.File
import kotlin.math.floor
import kotlin.math.pow

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2024/day17-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2024/day17.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2024/day17.txt") }
}

enum class Opcode {
    ADV,
    BXL,
    BST,
    JNZ,
    BXC,
    OUT,
    BDV,
    CDV,
}

data class Instruction(val opcode: Opcode, val operand: Int)

class Program(val stringRepresentation: String, code: List<Int>) {
    val instructions: List<Instruction>
    init {
        instructions = (0 until code.size step 2).map {
            Instruction(Opcode.entries[code[it]], code[it + 1])
        }
    }
}

class Machine(var registerA: Long, var registerB: Long, var registerC: Long) {
    var instructionPointer: Int = 0
    var output: String = ""

    fun runProgram(program: Program): String {
        while (instructionPointer < program.instructions.size) {
            val instruction = program.instructions[instructionPointer]
            when (instruction.opcode) {
                Opcode.ADV -> adv(instruction.operand)
                Opcode.BXL -> bxl(instruction.operand)
                Opcode.BST -> bst(instruction.operand)
                Opcode.JNZ -> jnz(instruction.operand)
                Opcode.BXC -> bxc(instruction.operand)
                Opcode.OUT -> out(instruction.operand)
                Opcode.BDV -> bdv(instruction.operand)
                Opcode.CDV -> cdv(instruction.operand)
            }
        }

        return output
    }

    fun comboOperand(operand: Int): Long {
        return when (operand) {
            0,1,2,3 -> operand.toLong()
            4 -> registerA
            5 -> registerB
            6 -> registerC
            else -> throw IllegalArgumentException("Invalid operand: $operand")
        }
    }

    fun adv(operand: Int) {
        registerA = div(operand)
        instructionPointer++
    }

    fun bxl(operand: Int) {
        registerB = registerB xor operand.toLong()
        instructionPointer++
    }

    fun bst(operand: Int) {
        registerB = comboOperand(operand) % 8
        instructionPointer++
    }

    fun jnz(operand: Int) {
        if (registerA != 0L) {
            instructionPointer = operand / 2
        } else {
            instructionPointer++
        }
    }

    fun bxc(operand: Int) {
        registerB = registerB xor registerC
        instructionPointer++
    }

    fun out(operand: Int) {
        if (output.isNotEmpty()) output += ","
        output += "${comboOperand(operand) % 8}"
        instructionPointer++
    }

    fun bdv(operand: Int) {
        registerB = div(operand)
        instructionPointer++
    }

    fun cdv(operand: Int) {
        registerC = div(operand)
        instructionPointer++
    }

    private fun div(operand: Int): Long {
        val newValue = registerA.toDouble() / 2.0.pow(comboOperand(operand).toDouble())
        return floor(newValue).toLong()
    }

    fun copy(registerA: Long = this.registerA, registerB: Long = this.registerB, registerC: Long = this.registerC): Machine {
        return Machine(registerA, registerB, registerC)
    }
}

fun puzzle1(fileName: String): String {
    val (machine, program) = loadMachineAndProgram(fileName)

    return machine.runProgram(program)
}

fun loadMachineAndProgram(fileName: String): Pair<Machine, Program> {
    val lines = File(fileName).readLines()

    val registerA = lines[0].substringAfter(": ").toLong()
    val registerB = lines[1].substringAfter(": ").toLong()
    val registerC = lines[2].substringAfter(": ").toLong()

    val codeString = lines[4].substringAfter(": ")
    val code = codeString.split(",").map { it.toInt() }

    return Pair(Machine(registerA, registerB, registerC), Program(codeString, code))
}

fun puzzle2(fileName: String): Long {
    val (machine, program) = loadMachineAndProgram(fileName)

    val current: Long = 0
    return buildRegisterToOutputProgram(current, machine, program) ?: -1
}


fun buildRegisterToOutputProgram(current: Long, machine: Machine, program: Program): Long? {
    fun runMachineWithRegisterA(registerA: Long): String {
        val runMachine = machine.copy(registerA = registerA)

        return runMachine.runProgram(program)
    }

    return (current..current + 7)
        .filter { program.stringRepresentation.endsWith(runMachineWithRegisterA(it)) }
        .mapNotNull {
            if (program.stringRepresentation == runMachineWithRegisterA(it)) {
                it
            } else {
                buildRegisterToOutputProgram(it * 8, machine, program)
            }
        }.minOrNull()
}
