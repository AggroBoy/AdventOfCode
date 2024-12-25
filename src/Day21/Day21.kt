package Day21

import util.Cache
import util.Coord
import util.printTimedOutput
import util.times
import java.io.File
import kotlin.math.abs

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day21-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day21.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day21-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day21.txt") }
}

abstract class Robot(var controlledBy: ControlRobot? = null) {
    abstract val keys: Map<Char, Coord>

    var currentButton = 'A'

    open fun movesForButton(targetButton: Char): String {
        val offset = keys[targetButton]!! - keys[currentButton]!!

        val xPart = (if (offset.x > 0) ">" else "<").repeat(abs(offset.x).toInt())
        val yPart = (if (offset.y > 0) "^" else "v").repeat(abs(offset.y).toInt())

        val result = if (xPart.isEmpty() && yPart.isEmpty()) {
             "A"
        } else if (yPart.isEmpty()) {
             xPart + "A"
        } else if (xPart.isEmpty()) {
             yPart + "A"
        } else if (!keys.containsValue(Coord(keys[currentButton]!!.x, keys[targetButton]!!.y))) {
             xPart + yPart + "A"
        } else if (!keys.containsValue(Coord(keys[targetButton]!!.x, keys[currentButton]!!.y))) {
             yPart + xPart + "A"
        } else if (offset.x < 0) {
             xPart + yPart + "A"
        } else if (offset.y < 0) {
             yPart + xPart + "A"
        } else {
             yPart + xPart + "A"
        }

        currentButton = targetButton
        return result
    }

    fun inputForOutput(output: String): Long {
        return output.map { targetButton ->
            inputLengthForButton(targetButton)
        }.sum()
    }

    open fun inputLengthForButton(targetButton: Char): Long {
        return movesForButton(targetButton).map { move ->
            controlledBy?.inputLengthForButton(move) ?: 1
        }.sum()
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

    private var cache: Cache<String, Long> = Cache()
    private var thing: MutableMap<String, Long> = mutableMapOf()

    override fun movesForButton(targetButton: Char): String {
        return when (currentButton to targetButton) {
            'A' to 'A' -> "A"
            'A' to '^' -> "<A"
            'A' to '<' -> "v<<A"
            'A' to '>' -> "vA"
            '^' to '^' -> "A"
            '^' to '<' -> "v<A"
            '^' to 'v' -> "vA"
            '^' to 'A' -> ">A"
            '<' to '^' -> ">^A"
            '<' to '<' -> "A"
            '<' to 'v' -> ">A"
            '<' to '>' -> ">>A"
            '<' to 'A' -> ">>^A"
            'v' to '^' -> "^A"
            'v' to '<' -> "<A"
            'v' to 'v' -> "A"
            'v' to '>' -> ">A"
            '>' to '<' -> "<<A"
            '>' to 'v' -> "<A"
            '>' to '>' -> "A"
            '>' to 'A' -> "^A"

            // 189357384273226

            'A' to 'v' -> "<vA" //
            '^' to '>' -> "v>A" //
            'v' to 'A' -> "^>A" //
            '>' to '^' -> "<^A" //

            else -> error("Invalid move")
        }
    }

    override fun inputLengthForButton(targetButton: Char): Long {
        val length = cache.getOrStore(cacheKey(targetButton)) {
            super.inputLengthForButton(targetButton)
        }
        currentButton = targetButton
        return length
    }

    fun cacheKey(targetButton: Char): String {
        return "$this:$currentButton:$targetButton"
    }
}

fun puzzle1(fileName: String): Long {
    val lines = File(fileName).readLines()

    val result = lines.map { line ->
        val keypadRobot = KeypadRobot(buildControlRobotSequence(2))
        val length = keypadRobot.inputForOutput(line)

        line.substring(0, 3).toLong() * length
    }.sum()

    return result
}

fun puzzle2(fileName: String): Long {
    val lines = File(fileName).readLines()

    val result = lines.map { line ->
        val keypadRobot = KeypadRobot(buildControlRobotSequence(25))
        val length = keypadRobot.inputForOutput(line)

        line.substring(0, 3).toLong() * length
    }.sum()

    return result
}

fun buildControlRobotSequence(depth: Int): ControlRobot {
    var controlRobot = ControlRobot()
    (depth - 1).times {
        controlRobot = ControlRobot(controlRobot)
    }
    return controlRobot
}
