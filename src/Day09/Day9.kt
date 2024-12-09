package Day09

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day9-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day9.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day9-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day9.txt") }
}

fun puzzle1(fileName: String): Long {
    val fs = loadBlockFilesystem(fileName)

    var headIndex = 0
    var tailIndex = fs.indexOfLast { it != null }
    val output = mutableListOf<Long?>()

    while (headIndex <= tailIndex) {
        if( fs[headIndex] != null ) {
            output.add(fs[headIndex])
            headIndex++
        } else {
            output.add(fs[tailIndex])
            headIndex++
            do tailIndex-- while (fs[tailIndex] == null)
        }
    }

    return output.checksum()
}

private fun loadBlockFilesystem(fileName: String): MutableList<Long?> {
    return File(fileName).readLines()[0].flatMapIndexed { index, it ->
        val length = it.toString().toInt()
        if (index % 2 == 0) {
            val fileId = index / 2
            List(length) { fileId.toLong() }
        } else {
            List(length) { null }
        }
    }.toMutableList()
}

data class FsFile(val fileId: Long?, val length: Int, var checked: Boolean = false)

fun puzzle2(fileName: String): Long {
    val fs = loadTableFilesystem(fileName)

    while (fs.any { it.fileId != null && !it.checked }) {
        val filePos = fs.indexOfLast { it.fileId != null && !it.checked }
        val file = fs[filePos]
        file.checked = true

        val spacePos = fs.indexOfFirst { it.fileId == null && it.length >= file.length }
        if (spacePos != -1 && spacePos < filePos) {
            val space = fs[spacePos]
            fs[filePos] = file.copy(fileId = null, checked = false)
            if (space.length > file.length) {
                fs[spacePos] = space.copy(length = space.length - file.length)
            } else {
                fs.removeAt(spacePos)
            }
            fs.add(spacePos, file)
        }
    }

    return fs.flatMap { file -> List(file.length) { file.fileId } }.toMutableList().checksum()
}

private fun loadTableFilesystem(fileName: String) = File(fileName).readLines()[0].mapIndexed { index, it ->
    val length = it.toString().toInt()
    if (index % 2 == 0) {
        val fileId = index / 2
        FsFile(fileId.toLong(), length)
    } else {
        FsFile(null, length)
    }
}.toMutableList()

fun MutableList<Long?>.checksum(): Long {
    return this.mapIndexed { pos, fileId ->
        when (fileId) {
            null -> 0
            else -> pos * fileId
        }
    }.sum()
}
