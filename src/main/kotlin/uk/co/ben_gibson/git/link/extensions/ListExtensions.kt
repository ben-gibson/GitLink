package uk.co.ben_gibson.git.link.extensions

fun <T> List<T>.replaceAt(index: Int, value: T): List<T> {
    val mutable = this.toMutableList()

    mutable[index] = value

    return mutable.toList()
}