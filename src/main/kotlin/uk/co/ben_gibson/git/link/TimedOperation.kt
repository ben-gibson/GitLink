package uk.co.ben_gibson.git.link

inline fun timeOperation(operation: () -> Unit) : Long {
    val startTime = System.currentTimeMillis()

    operation()

    val endTime = System.currentTimeMillis()

    return (endTime - startTime)
}