package uk.co.ben_gibson.git.link.git

data class Commit(private val hash: String) {
    init {
        require(hash.isNotBlank()) { "Commit hash cannot be blank" }
        require(hash.length >= 6) { "Commit hash must be at least 6 characters" }
    }

    val shortHash: String
        get() = hash.substring(0, minOf(6, hash.length))

    override fun toString(): String = hash
}