package Day07

import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day7-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day7.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day7-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day7.txt") }
}

fun puzzle1(s: String): Long {
    val calibrations = loadCalibrations(s)

    return calibrations.filter { calibrationCanMatch(it.first, it.second) }.sumOf { it.first }
}

fun calibrationCanMatch(target: Long, calibration: List<Long>): Boolean {
    return recurseCalibrationList(target, calibration.first(), calibration.drop(1))
}

fun recurseCalibrationList(target: Long, totalSoFar: Long, remaining: List<Long>): Boolean {
    if (remaining.isEmpty()) {
        return totalSoFar == target
    } else if (totalSoFar > target) {
        return false
    } else {
        return recurseCalibrationList(target, totalSoFar * remaining.first(), remaining.drop(1)) ||
                recurseCalibrationList(target, totalSoFar + remaining.first(), remaining.drop(1))
    }
}

fun puzzle2(s: String): Long {
    val calibrations = loadCalibrations(s)

    return calibrations.filter { calibrationCanMatchExtended(it.first, it.second) }.sumOf { it.first }
}

fun calibrationCanMatchExtended(target: Long, calibration: List<Long>): Boolean {
    return recurseCalibrationListExtended(target, calibration.first(), calibration.drop(1))
}

fun recurseCalibrationListExtended(target: Long, totalSoFar: Long, remaining: List<Long>): Boolean {
    if (remaining.isEmpty()) {
        return totalSoFar == target
    } else if (totalSoFar > target) {
        return false
    } else {
        return recurseCalibrationListExtended(target, totalSoFar * remaining.first(), remaining.drop(1)) ||
                recurseCalibrationListExtended(target, totalSoFar + remaining.first(), remaining.drop(1)) ||
                recurseCalibrationListExtended(target, "${totalSoFar}${remaining.first()}".toLong(), remaining.drop(1))
    }
}

fun loadCalibrations(fileName: String): List<Pair<Long, List<Long>>> {
    return File(fileName).readLines().map {
        val target = it.substringBefore(":").toLong()
        val numbers = it.substringAfter(":").trim().split(" ").map { it.trim().toLong() }
        Pair(target, numbers)
    }
}
