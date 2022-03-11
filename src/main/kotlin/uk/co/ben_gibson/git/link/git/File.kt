package uk.co.ben_gibson.git.link.git

/**
 * Represents a repository file, where the path is relative to the repository it lives in.
 */
data class File private constructor(val name : String, val path : String) {
    companion object {
        fun create(name: String, path: String) = File(name, path.replace(name, "").trimEnd('/'))
    }
}
