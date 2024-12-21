package util

class Cache<K, V> {
    val map: MutableMap<K, V> = mutableMapOf()

    fun getOrStore(key: K, lambda: (K) -> V): V {
        return map[key] ?: lambda(key).also { map[key] = it }
    }

    fun flush() = map.clear()
}