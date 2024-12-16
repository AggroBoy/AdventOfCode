package Day14

import util.*
import java.io.File
import kotlin.collections.MutableSet as MutableSet1

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day14-test.txt", Coord(11, 7)) }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day14.txt", Coord(101, 103)) }
    println("Running puzzle 2")
    puzzle2("input/day14.txt", Coord(101, 103))
}

data class Robot(val position: Coord, val velocity: Coord)

fun puzzle1(fileName: String, roomSize: Coord): Int {
    var robots = loadRobots(fileName)

    100.times {
        robots = robots.map { robot ->
            val newRobot = robot.copy(position = robot.position.wrappingAdd(robot.velocity, roomSize))
            newRobot
        }
    }

    var halfX = roomSize.x / 2
    val halfY = roomSize.y / 2

    var topLeft = robots.filter { it.position.x < halfX  && it.position.y < halfY }.size
    var topRight = robots.filter { it.position.x > halfX && it.position.y < halfY }.size
    var bottomLeft = robots.filter { it.position.x < halfX && it.position.y > halfY }.size
    var bottomRight = robots.filter { it.position.x > halfX && it.position.y > halfY }.size


    return topLeft * topRight * bottomLeft * bottomRight
}

fun Coord.wrappingAdd(other: Coord, roomSize: Coord): Coord {
    var newCoord = Coord((this.x + other.x) % roomSize.x, (this.y + other.y) % roomSize.y)
    if (newCoord.x < 0) newCoord = newCoord.copy(x = roomSize.x + newCoord.x)
    if (newCoord.y < 0) newCoord = newCoord.copy(y = roomSize.y + newCoord.y)
    return newCoord
}


fun puzzle2(fileName: String, roomSize: Coord): Int {
    var robots = loadRobots(fileName)

    var seconds = 0
    val history: MutableSet1<List<Robot>> = mutableSetOf()
    while (true) {
        if (history.contains(robots)) {
            println("repeat at ${seconds}")
            readln()
        }
        history.add(robots)

        val room = Array(roomSize.y.toInt()) { CharArray(roomSize.x.toInt()) { '.' } }
        robots.forEach { robot ->
            room[(robot.position.y).toInt()][(robot.position.x).toInt()] = '#'
        }
        val strings = room.map{ it.joinToString("") }
        if (strings.any { it.contains("######")}) {

            println("Seconds: $seconds")

            room.forEach { row -> println(row.joinToString("")) }
            readLine()
        }

        robots = robots.map { robot ->
            val newRobot = robot.copy(position = robot.position.wrappingAdd(robot.velocity, roomSize))
            newRobot
        }

        seconds++
    }
}

fun loadRobots(fileName: String): List<Robot> {
    return File(fileName).readLines().map { line ->
        val values = """p=([\d-]+),([\d-]+) v=([\d-]+),([\d-]+)""".toRegex().find(line)!!.groupValues
        Robot(Coord(values[1].toLong(), values[2].toLong()), Coord(values[3].toLong(), values[4].toLong()))
    }
}
