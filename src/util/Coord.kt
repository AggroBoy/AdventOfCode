package util

typealias Coord = Pair<Int, Int>

val Coord.x get() = this.first
val Coord.y get() = this.second
operator fun Coord.plus(other: Coord) = Coord(this.x + other.x, this.y + other.y)
operator fun Coord.minus(other: Coord) = Coord(this.x - other.x, this.y - other.y)