package twenty25.day12

import util.Coord
import util.printTimedOutput
import java.io.File

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/2025/day12-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/2025/day12.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/2025/day12-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/2025/day12.txt") }
}

data class Region(
    val size: Coord,
    val shapeCounts: List<Int>,
    val map: MutableSet<Coord> = mutableSetOf()
)

typealias Shape = Set<Coord>
fun Shape.render(): String {
    return (0..this.maxOf { it.y }).joinToString("\n") { y ->
        (0..this.maxOf { it.x }).joinToString(" ") { x ->
            if (this.contains(Coord(x, y)))
                "#"
            else
                "."
        }
    }
}

fun Shape.flipHorizontal() = this.map{ Coord(this.maxOf{it.x} - it.x, it.y) }.toSet()
fun Shape.flipVertical() = this.map{ Coord(it.x, this.maxOf{it.y} - it.y) }.toSet()
fun Shape.rotateRight() = this.map{ Coord(this.maxOf{it.y} - it.y, it.x) }.toSet()
fun Shape.allRotations(): Set<Shape> {
    val one = this.rotateRight()
    val two = one.rotateRight()
    val three = two.rotateRight()
    return setOf(this, one, two, three)
}

fun Shape.size() = Coord(this.maxOf { it.x }, this.maxOf { it.y })
fun Shape.area() = (this.maxOf { it.x } + 1) * (this.maxOf { it.y } + 1)

fun puzzle1(fileName: String): Int {
    val (shapes, regions) = laodShapesAndRegions(fileName)

    val shapePermutations = generateShapePermutations(shapes)

    val shapeWidth = 3
    val shapeHeight = 3

    val definiteYes = regions.filter { region ->
        val numberOfShapesX = region.size.x.floorDiv(shapeWidth)
        val numberOfShapesY = region.size.y.floorDiv(shapeHeight)

        numberOfShapesX * numberOfShapesY >= region.shapeCounts.sum()
    }

    val definiteNo = regions.filter { region ->
        val regionArea = region.size.x * region.size.y
        val requiredArea = region.shapeCounts.mapIndexed { shape, count ->
            shapes[shape].size * count
        }.sum()

        regionArea < requiredArea
    }

    val possible = regions.count { region ->
        region.shapeCounts.flatMapIndexed { shapeNumber, shapeCount -> List(shapeCount) {shapeNumber} }.map { shapeNumber ->
            val result = findPermuatationThatFits(shapePermutations[shapeNumber], region)
            if (result != null) {
                addPermutationToMap(result.second, region.map, result.first)
            }
            result != null
        }.reduce { acc, v -> acc && v }
    }

    return possible
}

fun findPermuatationThatFits(
    shape: Set<Shape>,
    region: Region,
): Pair<Coord, Shape>? {
    val map = region.map
    for (y in (0..region.size.y)) {
        for (x in (0..region.size.x)) {
            val origin = Coord(x, y)
            shape.find { it.none { map.contains(origin + it) } }?.let { return Coord(x, y) to it }
        }
    }
    return null
}

fun addPermutationToMap(permutation: Shape, map: MutableSet<Coord>, origin: Coord) {
    permutation.forEach {
        map.add(origin + it)
    }
}


fun generateShapePermutations(shapes: List<Shape>): List<Set<Shape>> {
    return shapes.map {
        (
            it.allRotations() +
            it.flipHorizontal().allRotations() +
            it.flipVertical().allRotations()
            ).toSet()
    }
}

fun laodShapesAndRegions(fileName: String): Pair<List<Shape>, List<Region>> {
    val lines = File(fileName).readLines() + listOf("")
    val buffer = mutableListOf<String>()
    var regions = listOf<Region>()
    val shapes = mutableListOf<Shape>()

    lines.forEach { line ->
        if (line.isBlank()) {
            if (buffer[0].contains('x')) {
                regions = buffer.map { line ->
                    val width = line.substringBefore('x').toInt()
                    val height = line.substringAfter('x').substringBefore(':').toInt()
                    val counts = line.substringAfter(':').trim().split(' ').map{ it.toInt() }
                    Region(Coord(width, height), counts)
                }
            } else {
                shapes.add( buffer.subList(1, buffer.size).flatMapIndexed { y, line ->
                    line.mapIndexed { x, char ->
                        if (char == '#')
                            Coord(x,y)
                        else
                            null
                    }.filterNotNull()
                }.toSet() )
            }
            buffer.clear()
        } else {
            buffer.add(line)
        }
    }

    return shapes to regions
}

fun puzzle2(fileName: String): Long {
    return 0L
}
