package util

// This is a simple cache designed to be used for memoization of
// function calls or simple code blocks. It's not thread safe, and
// doesn't really do anything you can't do with a Map, but it's
// null-safe, which is useful for memoization, and has a nice syntax.
// Almost all interactions should be getOrStore()
class Cache<K, V> {
    val map: MutableMap<K, V> = mutableMapOf()

    fun getOrStore(key: K, lambda: (K) -> V): V {
        try {
            // Use getValue because it throws, so we can tell the difference
            // between a null value and a missing value
            return map.getValue(key)
        } catch (e: NoSuchElementException) {
            return lambda(key).also { map.put(key, it) }
        }
    }

    fun get(key: K): V = map.getValue(key)
    fun put(key: K, value: V) {
        map[key] = value
    }
    fun contains(key: K): Boolean = map.containsKey(key)
    fun remove(key: K) = map.remove(key)
    fun flush() = map.clear()
}