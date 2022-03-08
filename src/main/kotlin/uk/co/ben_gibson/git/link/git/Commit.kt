package uk.co.ben_gibson.git.link.git

data class Commit(private val hash: String) {

    val shortHash: String
        get() {
            return hash.substring(0, 6)
        }

    override fun toString(): String {
        return hash
    }
}