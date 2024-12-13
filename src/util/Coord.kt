package util

typealias Coord = Pair<Long, Long>

fun Coord(x: Int, y: Int) = Coord(x.toLong(), y.toLong())
val Coord.x get() = this.first
val Coord.y get() = this.second
operator fun Coord.plus(other: Coord) = Coord(this.x + other.x, this.y + other.y)
operator fun Coord.minus(other: Coord) = Coord(this.x - other.x, this.y - other.y)
operator fun Coord.times(scalar: Long) = Coord(this.x * scalar, this.y * scalar)
