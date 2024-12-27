package twenty24.day14

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import util.Coord

class Day14Test : FunSpec({

    test("wrappingAdd") {
        val roomSize = Coord(10, 10)
        Coord(1, 1).wrappingAdd(Coord(1,1), roomSize) shouldBe Coord(2, 2)
        Coord(1, 1).wrappingAdd(Coord(9,9), roomSize) shouldBe Coord(0, 0)
        Coord(8, 8).wrappingAdd(Coord(4,4), roomSize) shouldBe Coord(2, 2)
        Coord(2, 2).wrappingAdd(Coord(-4,-4), roomSize) shouldBe Coord(8, 8)
    }
})
