package Day09

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day9-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day9.txt") }
//    printTimedOutput("Puzzle 2 test") { puzzle2("input/day9-test.txt") }
//    printTimedOutput("Puzzle 2     ") { puzzle2("input/day9.txt") }
}

fun puzzle1(fileName: String): Long {
    val fs = File(fileName).readLines()[0].flatMapIndexed { index, it ->
        val length = it.toString().toInt()
        if (index % 2 == 0) {
            val fileId = index / 2
            List(length) { fileId.toLong() }
        } else {
            List(length) { null }
        }
    }.toMutableList()

    var firstSpace = fs.indexOfFirst { it == null }
    var lastFileChunk = fs.indexOfLast { it != null }
    while (firstSpace < lastFileChunk) {
        fs[firstSpace] = fs[lastFileChunk]
        fs[lastFileChunk] = null
        firstSpace = fs.indexOfFirst { it == null }
        lastFileChunk = fs.indexOfLast { it != null }
    }

    return fs.mapIndexed { pos, fileId ->
        when (fileId) {
            null -> 0
            else -> pos * fileId
        }
    }.sum()
}

fun puzzle2(fileName: String): Int {
    return -1
}
