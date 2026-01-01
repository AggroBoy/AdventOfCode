package util

fun <T> Collection<T>.permutations(): List<List<T>> {
    var allBits = 0
    (0..this.size-1).forEach { allBits = (allBits shl 1) or 1 }

    val result = mutableListOf<List<T>>()

    (1..allBits).forEach { bitMap ->
        val bitString = bitMap.toUInt().toString(2).padStart(this.size, '0')
        val permutation = this.filterIndexed { i, _ ->
            bitString[i] == '1'
        }
        result.add(permutation)
    }

    return result.sortedBy { it.size }
}