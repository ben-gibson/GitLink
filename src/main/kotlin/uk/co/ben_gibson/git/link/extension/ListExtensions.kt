package uk.co.ben_gibson.git.link.extension

fun <T> List<T>.replaceAt(index: Int, value: T): List<T> {
    val mutable = this.toMutableList()

    mutable[index] = value

    return mutable.toList()
}