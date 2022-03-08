package uk.co.ben_gibson.git.link

inline fun <T> timeOperation(operation: () -> T) : Pair<T, Long> {
    val startTime = System.nanoTime()

    val result = operation()

    val endTime = System.nanoTime()

    val totalTimeSeconds = endTime - startTime

    return Pair(result, totalTimeSeconds)
}