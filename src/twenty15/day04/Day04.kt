@file:OptIn(ExperimentalStdlibApi::class)

package twenty15.day04

import util.printTimedOutput
import java.io.File
import java.security.MessageDigest

fun main() {
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2015/day04.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2015/day04.txt") }
}

fun puzzle1(fileName: String): Long {
    val key = File(fileName).readLines()[0]
    val md = MessageDigest.getInstance("MD5")

    var i = 0L
    do {
        val candidate = key + i.toString()
        val digest = md.digest(candidate.toByteArray())
        if (digest.toHexString().startsWith("00000")) {
            return i
        }
        i++
    } while (true)
}

fun puzzle2(fileName: String): Long {
    val key = File(fileName).readLines()[0]
    val md = MessageDigest.getInstance("MD5")

    var i = 0L
    do {
        val candidate = key + i.toString()
        val digest = md.digest(candidate.toByteArray())
        if (digest.toHexString().startsWith("000000")) {
            return i
        }
        i++
    } while (true)
}
