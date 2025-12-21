package util

enum class Direction(val coord: Coord) {
    UP(Coord(0, 1)),
    DOWN(Coord(0, -1)),
    LEFT(Coord(-1, 0)),
    RIGHT(Coord(1, 0));

    fun turnRight(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }

    fun turnLeft(): Direction {
        return when (this) {
            UP -> LEFT
            LEFT -> DOWN
            DOWN -> RIGHT
            RIGHT -> UP
        }
    }
}

// This is a legacy class. I used to use screen-style coord systems, where y increased downward
// and this direction class preserves that so old puzzles keep working. I should probably refactor
// then to use Direction.
enum class ScreenDirection(val coord: Coord) {
    UP(Coord(0, -1)),
    DOWN(Coord(0, 1)),
    LEFT(Coord(-1, 0)),
    RIGHT(Coord(1, 0));

    fun turnRight(): ScreenDirection {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }

    fun turnLeft(): ScreenDirection {
        return when (this) {
            UP -> LEFT
            LEFT -> DOWN
            DOWN -> RIGHT
            RIGHT -> UP
        }
    }
}