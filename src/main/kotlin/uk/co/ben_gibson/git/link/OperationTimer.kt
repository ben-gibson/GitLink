package uk.co.ben_gibson.git.link

inline fun timeOperation(operation: () -> Unit) : Long {
    val startTime = System.nanoTime()

    operation()

    val endTime = System.nanoTime()

    return endTime - startTime
}