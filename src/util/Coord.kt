package util

import kotlin.math.absoluteValue

//typealias Coord = Pair<Long, Long>
data class Coord(val x: Long, val y: Long) {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())

    operator fun plus(other: Coord) = Coord(this.x + other.x, this.y + other.y)
    operator fun plus(direction: ScreenDirection): Coord = this + direction.coord
    operator fun plus(direction: Direction): Coord = this + direction.coord
    operator fun minus(other: Coord) = Coord(this.x - other.x, this.y - other.y)

    operator fun times(scalar: Long) = Coord(this.x * scalar, this.y * scalar)

    fun manhattanDistanceFrom(other: Coord) = Math.abs(this.x - other.x) + Math.abs(this.y - other.y)

    override fun toString() = "$x,$y"
}

fun Long.squared(): Long = this * this
fun Long.sqroot(): Double = Math.sqrt(this.toDouble())

data class Coord3d(val x: Long, val y: Long, val z: Long) {
    constructor(x: Int, y: Int, z: Int) : this(x.toLong(), y.toLong(), z.toLong())

    operator fun plus(other: Coord3d) = Coord3d(this.x + other.x, this.y + other.y, this.z + other.z)
    operator fun minus(other: Coord3d) = Coord3d(this.x - other.x, this.y - other.y, z=this.z - other.z)

    operator fun times(scalar: Long) = Coord3d(this.x * scalar, this.y * scalar, this.z * scalar)

//    fun manhattanDistanceFrom(other: Coord3d) = Math.abs(this.x - other.x) + Math.abs(this.y - other.y)
    fun disatanceFrom(other: Coord3d): Double {
        return (
            (this.x - other.x).absoluteValue.squared() +
            (this.y - other.y).absoluteValue.squared() +
            (this.z - other.z).absoluteValue.squared()
        ).sqroot()
    }

    override fun toString() = "$x,$y,$z"
}
