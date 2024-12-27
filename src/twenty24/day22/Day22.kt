package twenty24.day22

import util.printTimedOutput
import util.times
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day22-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day22.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day22-test2.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day22.txt") }
}

fun puzzle1(fileName: String): Long {
    return File(fileName).readLines().map { line ->
        var num = line.toLong()

        2000.times {
            num = newSecretNumber(num)
        }
        num
    }.sum()
}

data class SequenceOfFour(
    val first: Long,
    val second: Long,
    val third: Long,
    val fourth: Long
)

fun puzzle2(fileName: String): Long {
    val numbers = File(fileName).readLines().map { line ->
        var num = line.toLong()

        line to (0 until 2000).map {
            num = newSecretNumber(num)
            num
        }.map { it % 10}
    }.toMap()

    val totals: MutableMap<SequenceOfFour, Long> = mutableMapOf()
    numbers.keys.forEach() { source ->
        val sequence = numbers[source] ?: error("No sequence found for $source")
        val seenSequences = mutableSetOf<SequenceOfFour>()

        (4 until sequence.size).forEach { index ->
            val key = SequenceOfFour(
                sequence[index - 3] - sequence[index - 4],
                sequence[index - 2] - sequence[index - 3],
                sequence[index - 1] - sequence[index - 2],
                sequence[index] - sequence[index - 1]
            )
            if (!seenSequences.contains(key)) {
                seenSequences.add(key)
                totals[key] = totals.getOrDefault(key, 0).plus(sequence[index])
            }
        }
    }

    return totals.values.sortedBy { it }.last()
}

fun newSecretNumber(secretNumber: Long): Long {
    var num = ((secretNumber * 64) mix secretNumber).prune()
    num = ((num / 32) mix num).prune()
    num = ((num * 2048) mix num).prune()

    return num
}

// To prune the secret number, calculate the value of the secret number modulo 16777216.
// Then, the secret number becomes the result of that operation. (If the secret number
// is 100000000 and you were to prune the secret number, the secret number would become
// 16113920.)
fun Long.prune(): Long {
    return this % 16777216
}

// To mix a value into the secret number, calculate the bitwise XOR of the given value
// and the secret number. Then, the secret number becomes the result of that operation.
// (If the secret number is 42 and you were to mix 15 into the secret number, the
// secret number would become 37.)
infix fun Long.mix(other: Long): Long {
    return this xor other
}
