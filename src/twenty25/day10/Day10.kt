package twenty25.day10

import util.Cache
import util.printTimedOutput
import java.io.File
import kotlin.math.max

fun main() {
//    printTimedOutput("Puzzle 1 test") { puzzle1("input/2025/day10-test.txt") }
//    printTimedOutput("Puzzle 1     ") { puzzle1("input/2025/day10.txt") }
//    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day10-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day10.txt") }
}

data class Machine (
    val lightsTarget: Int,
    val joltageTargets: List<Int>,
    val buttons: List<Int>,
    val buttonsInts: List<List<Int>>
) {
    val buttonPressCache = Cache<List<Int>, List<List<Int>>?>()
    var minPressesSoFar = Int.MAX_VALUE

    fun buttonPressesToTargetLights(): List<Int> {
        // What combination of buttons XORed together gives the target
        buttons.permutations().map { list ->
            val permutationResult = list.reduce { acc, it -> acc xor it}
            if ( permutationResult == lightsTarget)
                return list
        }
        throw Exception("Not found")
    }

    fun walkButtonPressesToTargetJoltage(currentJoltages: List<Int>): List<List<Int>>?  {
        if (currentJoltages.filterIndexed { i, joltage ->
                joltage > joltageTargets[i]
            }.isNotEmpty()// || presses.size > minPressesSoFar
        )
            return null

        if (currentJoltages == joltageTargets) {
            //minPressesSoFar = presses.size
            println("solutionFound!")
            return listOf()
        }

        val currentfocus = joltageTargets.mapIndexedNotNull { i, target ->
            if (currentJoltages[i] != target)
                i
            else
                null
        }.maxBy { joltageTargets[it] }

        return buttonsInts.filter { it.contains(currentfocus) }.map { button ->
            //val newPresses = 1
            val newPresses = currentJoltages.mapIndexedNotNull { i, _ ->
                if (button.contains(i)) (joltageTargets[i] - currentJoltages[i]) - joltageTargets.size
//                if (button.contains(i)) (joltageTargets[i] - currentJoltages[i]) - 5
                else null
            }.minOf { max(it, 1) }

            List(newPresses) { button } to currentJoltages.mapIndexed { i, joltage ->
                if (button.contains(i)) joltage + newPresses
                else joltage
            }
        }.mapNotNull { (newPresses, newJoltages) ->
            buttonPressCache.getOrStore(newJoltages) { walkButtonPressesToTargetJoltage(newJoltages) }?.let { it + newPresses }
        }.minByOrNull { it.size }
    }


    companion object {
        fun load(string: String): Machine {
            var target = 0
            val targetStr = string.substring(1, string.indexOf(']')).reversed()
            targetStr.forEach {
                target = target shl 1
                target = target or when (it)
                {
                    '#' -> 1
                    else -> 0
                }
            }

            val buttonStr = string.substringAfter(']').substringBefore('{').trim()
            val buttonsInts = buttonStr
                .split(" ")
                .map { it.substringAfter('(').substringBefore(')') }
                .map { it.split(',').map{ it.toInt() } }.sortedByDescending { it.size }
            val buttonsBitmap = buttonsInts
                .map {
                    var value = 0
                    it.forEach { value = (1 shl it) or value }
                    value
                }

            val joltageString = string.substringAfter('{').substringBefore('}')
            val joltageTargets = joltageString.split(",").map { it.toInt() }

            return Machine(target, joltageTargets, buttonsBitmap, buttonsInts)
        }
    }
}

private fun <T>List<T>.permutations(): List<List<T>> {
    var allBits = 0
    (0..this.size-1).forEach { allBits = (allBits shl 1) or 1 }

    val result = mutableListOf<List<T>>()

    (1..allBits).forEach { bitMap ->
        val bitString = bitMap.toUInt().toString(2).padStart(this.size, '0')
        val permutation = this.filterIndexed { i, _ ->
            bitString[i] == '1'
        }
        result.add(permutation)
    }

    return result.sortedBy { it.size }
}

fun puzzle1(fileName: String): Int {
    val machines = File(fileName).readLines().map { Machine.load(it) }

    val a = machines.map { it.buttonPressesToTargetLights() }
    return a.sumOf { it.size }
}


fun puzzle2(fileName: String): Int {
    val machines = File(fileName).readLines().map { Machine.load(it) }

    val a = machines.map {
        val answer = it.walkButtonPressesToTargetJoltage(it.joltageTargets.map { 0 })
        if (answer != null)
            answer
        else
            throw IllegalArgumentException("Unsolvable machine!")
    }
    return a.sumOf { it.size }
}
