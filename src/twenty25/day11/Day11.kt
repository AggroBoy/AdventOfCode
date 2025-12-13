package twenty25.day11

import util.Cache
import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2025/day11-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2025/day11.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day11-test2.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day11.txt") }
}

fun puzzle1(fileName: String): Long {
    val nodes = File(fileName).readLines().associate { line ->
        val name = line.substringBefore(':')
        val outputs = line.substringAfter(':').split(' ').map { it.trim() }.filterNot { it.isEmpty() }

        name to outputs
    }

    fun walkNodes(node: String): Long {
        if (node == "out")
            return 1

        return nodes[node]?.sumOf { walkNodes(it) } ?: throw Exception("No node")
    }

    return walkNodes("you")
}

fun puzzle2(fileName: String):Long {
    val nodeCache = Cache<Pair<String, String>, Long?>()

    val nodes = File(fileName).readLines().associate { line ->
        val name = line.substringBefore(':')
        val outputs = line.substringAfter(':').split(' ').map { it.trim() }.filterNot { it.isEmpty() }

        name to outputs
    }

    fun walkNodes(node: String, target: String): Long? {
        if (node == target)
            return 1L
        else if (node == "out")
            return null

        return nodes[node]!!.mapNotNull { newNode ->
            nodeCache.getOrStore(newNode to target) { walkNodes(newNode, target) }
        }.sum()
    }

    val svrToDac = walkNodes("svr", "dac") ?: throw Exception("no answer")
    val dacToFft = walkNodes("dac", "fft") ?: throw Exception("no answer")
    val fftToOut = walkNodes("fft", "out") ?: throw Exception("no answer")
    val routeOne = svrToDac * dacToFft * fftToOut

    val svrToFft = walkNodes("svr", "fft") ?: throw Exception("no answer")
    val fftToDac = walkNodes("fft", "dac") ?: throw Exception("no answer")
    val dacToOut = walkNodes("dac", "out") ?: throw Exception("no answer")
    val routeTwo = svrToFft * fftToDac * dacToOut

    return routeOne + routeTwo
}
