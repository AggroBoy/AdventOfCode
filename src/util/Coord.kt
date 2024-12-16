package util

//typealias Coord = Pair<Long, Long>
data class Coord(val x: Long, val y: Long) {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())

    operator fun plus(other: Coord) = Coord(this.x + other.x, this.y + other.y)
    operator fun minus(other: Coord) = Coord(this.x - other.x, this.y - other.y)

    operator fun times(scalar: Long) = Coord(this.x * scalar, this.y * scalar)
}
