package twenty15.day07

import util.Cache
import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2015/day07-test.txt", "g") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2015/day07.txt", "a") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2015/day07.txt", "a") }
}

val wireCache = Cache<String, UShort>()
fun getWireValue(wire: String, commands: Map<String, String>): UShort {
    return wireCache.getOrStore(wire) { getWireValueWork(wire, commands) }
}

fun getWireValueWork(wire: String, commands: Map<String, String>): UShort {
    if (wire.all { it.isDigit() } )
        return wire.toUShort()

    val command = commands[wire] ?: throw IllegalArgumentException("No such wire: $wire")

    if (command.all { it.isDigit() } )
        return command.toUShort()

    if (!command.contains(' '))
        return getWireValue(command, commands)

    if (command.startsWith("NOT ")) return getWireValue(command.substringAfterLast("NOT "), commands).inv()

    val one = command.substringBefore(" ")
    val operation = command.substringAfter(" ").substringBeforeLast(" ")
    val two = command.substringAfterLast(" ")

    return when (operation) {
        "AND" -> getWireValue(one, commands) and getWireValue(two, commands)
        "OR" -> getWireValue(one, commands) or getWireValue(two, commands)
        "RSHIFT" -> (getWireValue(one, commands).toUInt() shr two.toInt()).toUShort()
        "LSHIFT" -> (getWireValue(one, commands).toUInt() shl two.toInt()).toUShort()
        else -> throw IllegalArgumentException("Bad operation: $operation")
    }
}

fun puzzle1(fileName: String, wire: String): UShort {
    wireCache.flush()

    val lines = File(fileName).readLines()

    val instructions = lines.map { line ->
        val output = line.substringAfterLast(" -> ")
        val command = line.substringBeforeLast(" -> ")

        output to command
    }.toMap()

    return getWireValue(wire ?: throw IllegalArgumentException(), instructions)
}

fun puzzle2(fileName: String, wire: String): UShort {
    val lines = File(fileName).readLines()

    val instructions = lines.map { line ->
        val output = line.substringAfterLast(" -> ")
        val command = line.substringBeforeLast(" -> ")

        output to command
    }.toMap().toMutableMap()

    instructions["b"] = puzzle1(fileName, "a").toString()
    wireCache.flush()

    return getWireValue(wire ?: throw IllegalArgumentException(), instructions)
}
