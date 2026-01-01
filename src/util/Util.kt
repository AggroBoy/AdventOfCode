package util

import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource


@OptIn(ExperimentalTime::class)
fun <T>printTimedOutput(title: String, outputWidth: Int = 30, lambda: () -> T) {
    val timeStamp = TimeSource.Monotonic.markNow()
    val result = lambda()
    println("%-${outputWidth}s %10s".format("$title : $result", "(${timeStamp.elapsedNow().inWholeMilliseconds}ms)"))
}

fun Int.isOdd() = this % 2 != 0
fun Int.isEven() = this % 2 == 0

fun Int.times(lambda: () -> Unit) {
    (0 until this).forEach { lambda() }
}

