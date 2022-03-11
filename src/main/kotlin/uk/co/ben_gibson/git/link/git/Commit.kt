package uk.co.ben_gibson.git.link.git

data class Commit(private val hash: String) {

    val shortHash get() = hash.substring(0, 6)

    override fun toString() = hash
}