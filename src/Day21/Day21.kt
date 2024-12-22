package Day21

import util.Coord
import util.printTimedOutput
import util.times
import java.io.File
import kotlin.math.abs

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day21-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day21.txt") }
//    printTimedOutput("Puzzle 2 test") { puzzle2("input/day21-test.txt") }
//    printTimedOutput("Puzzle 2     ") { puzzle2("input/day21.txt") }
}

data class State(
    val currentKeys: Map<Robot, Char>
)

abstract class Robot(val controlledBy: ControlRobot? = null) {
    abstract val keys: Map<Char, Coord>

    open fun movesForButton(currentButton: Char, targetButton: Char): List<String> {
        val offset = keys[targetButton]!! - keys[currentButton]!!

        val xPart = (if (offset.x > 0) ">" else "<").repeat(abs(offset.x).toInt())
        val yPart = (if (offset.y > 0) "^" else "v").repeat(abs(offset.y).toInt())

        if (xPart.isEmpty() && yPart.isEmpty()) {
            return listOf("A")
        } else
        if (yPart.isEmpty()) {
            return listOf(xPart + "A")
        } else if (xPart.isEmpty()) {
            return listOf(yPart + "A")
        } else if (!keys.containsValue(Coord(keys[currentButton]!!.x, keys[targetButton]!!.y))) {
            return listOf(xPart + yPart + "A")
        } else if (!keys.containsValue(Coord(keys[targetButton]!!.x, keys[currentButton]!!.y))) {
            return listOf(yPart + xPart + "A")
        }

        return listOf(xPart + yPart + "A", yPart + xPart + "A")
    }

    fun inputOptionsForOutput(output: String): List<String> {
        var options: List<String> = listOf("")
        var currentButton = 'A'

        output.forEach { targetButton ->
            options = options.flatMap { option ->
                movesForButton(currentButton, targetButton).map { move ->
                    option + move
                }
            }
            currentButton = targetButton
        }

        return options
    }
}

class KeypadRobot(controlledBy: ControlRobot? = null): Robot(controlledBy) {
    override val keys: Map<Char, Coord> = mapOf(
        '0' to Coord(1, 0),
        'A' to Coord(2, 0),
        '1' to Coord(0, 1),
        '2' to Coord(1, 1),
        '3' to Coord(2, 1),
        '4' to Coord(0, 2),
        '5' to Coord(1, 2),
        '6' to Coord(2, 2),
        '7' to Coord(0, 3),
        '8' to Coord(1, 3),
        '9' to Coord(2, 3)
    )
}

class ControlRobot(controlledBy: ControlRobot? = null): Robot(controlledBy) {
    override val keys: Map<Char, Coord> = mapOf(
        '^' to Coord(1, 1),
        '<' to Coord(0, 0),
        'v' to Coord(1, 0),
        '>' to Coord(2, 0),
        'A' to Coord(2, 1),
    )
}

fun puzzle1(fileName: String): Int {
    val lines = File(fileName).readLines()

    val keypadRobot = KeypadRobot()
    val controlRobot = ControlRobot()

    val result = lines.map { line ->
        var candidates: List<String> = emptyList()
        candidates = keypadRobot.inputOptionsForOutput(line)

        2.times {
            candidates = candidates.flatMap { candidate ->
                controlRobot.inputOptionsForOutput(candidate)
            }
            val shortest = candidates.minBy { it.length }
            println("$line: ${shortest.length} ($shortest)}")
        }

        val shortest = candidates.minBy { it.length }

        line.substring(0, 3).toInt() * shortest.length
    }.sum()

    return result
}

fun puzzle2(fileName: String): Int {
    return -1
}
