package twenty24.day25

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day25-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day25.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day25-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day25.txt") }
}

data class Key(
    val pin1: Int,
    val pin2: Int,
    val pin3: Int,
    val pin4: Int,
    val pin5: Int
)
data class Lock(
    val pin1: Int,
    val pin2: Int,
    val pin3: Int,
    val pin4: Int,
    val pin5: Int
) {
    fun acceptsKey(key: Key): Boolean {
        return pin1 + key.pin1 <= 5 &&
            pin2 + key.pin2 <= 5 &&
            pin3 + key.pin3 <= 5 &&
            pin4 + key.pin4 <= 5 &&
            pin5 + key.pin5 <= 5
    }
}

fun puzzle1(fileName: String): Int {
    val (locks, keys) = loadLocksAndKeys(fileName)

    return locks.map { lock ->
        keys.count { key ->
            lock.acceptsKey(key)
        }
    }.sum()
}

fun loadLocksAndKeys(fileName: String): Pair<List<Lock>, List<Key>> {
    val keys: MutableList<Key> = mutableListOf()
    val locks: MutableList<Lock> = mutableListOf()

    File(fileName).readLines().chunked(8).map{ it.subList(0, 7) }.forEach { lines ->
        if (lines[0] == "#####") {
            locks.add(
                Lock(
                lines.indexOfFirst { it [0] == '.' } - 1,
                lines.indexOfFirst { it [1] == '.' } - 1,
                lines.indexOfFirst { it [2] == '.' } - 1,
                lines.indexOfFirst { it [3] == '.' } - 1,
                lines.indexOfFirst { it [4] == '.' } - 1
            ))
        } else {
            keys.add(
                Key(
                5 - lines.indexOfLast { it [0] == '.' },
                5 - lines.indexOfLast { it [1] == '.' },
                5 - lines.indexOfLast { it [2] == '.' },
                5 - lines.indexOfLast { it [3] == '.' },
                5 - lines.indexOfLast { it [4] == '.' }
            ))
        }
    }
    return locks to keys
}

fun puzzle2(fileName: String): Int {
    return -1
}
