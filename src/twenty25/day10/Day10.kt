package twenty25.day10

import util.isEven
import util.Cache
import util.printTimedOutput
import java.io.File
import kotlin.collections.map

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2025/day10-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2025/day10.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day10-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day10.txt") }
}

data class Machine (
    val lightsTarget: Int,
    val joltageTargets: List<Int>,
    val buttons: List<Int>,
    val buttonsInts: List<List<Int>>
) {
    val buttonPressCache = Cache<List<Int?>, Int?>()

    fun buttonPressesToTargetLights(): List<Int> {
        // What combination of buttons XORed together gives the target
        buttons.permutations().map { list ->
            val permutationResult = list.reduce { acc, it -> acc xor it}
            if ( permutationResult == lightsTarget)
                return list
        }
        throw Exception("Not found")
    }

    fun walkButtonPressesToTargetJoltage(currentJoltages: List<Int>): Int?  {
        if ( currentJoltages.all { it == 0 } ) return 0

        try {
            val options = mutableListOf<List<List<Int>>>()
            if (currentJoltages.all { it.isEven() }) options.add(emptyList())

            val joltageToPresses = mutableMapOf<List<Int>, List<List<Int>>>()
            buttonsInts.permutations().forEach { buttonSet ->
                val newJoltages = currentJoltages.mapIndexed { i, joltage ->
                    joltage - buttonSet.count { it.contains(i) }
                }
                if (newJoltages.all { it.isEven() && it >= 0 } ) {
                    if (joltageToPresses.getOrPut(newJoltages, { buttonSet }).size > buttonSet.size)
                        joltageToPresses[newJoltages] = buttonSet
                }
            }
            options.addAll( joltageToPresses.toList()
                .map { it.second }
            )

            if (options.isEmpty())
                return null

            return options.mapNotNull { pressed ->
                val newJoltages = currentJoltages.mapIndexed { i, joltage ->
                    joltage - pressed.count { it.contains(i) }
                }

                val halvedJoltages = newJoltages.map { it / 2 }
                val walkResult = buttonPressCache.getOrStore(halvedJoltages) { walkButtonPressesToTargetJoltage(halvedJoltages) }
                if (walkResult != null) pressed.size + (2 * walkResult)
                else null
            }.min()
        } catch (e: NoSuchElementException) {
            return null
        }
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
                .map { it.split(',').map{ it.toInt() } }.sortedBy { it.size }
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
        it.walkButtonPressesToTargetJoltage(it.joltageTargets.toMutableList() ) ?: throw Exception("Unworkable machine: $it")
    }
        return a.sum()
}
