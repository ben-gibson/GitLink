package uk.co.ben_gibson.git.link.url

class PathBuilder {
    private val parts:MutableList<String> = mutableListOf()

    fun withPart(parameter: String) : PathBuilder {
        parts.add(encode(parameter.trim('/')))
        return this
    }

    fun withParts(parameters: List<String>) : PathBuilder {
        parameters.forEach() { withPart(it) }
        return this
    }

    fun withSubPath(subPath: String) : PathBuilder {
        parts.add(subPath.trim('/'))
        return this
    }

    fun build() = "/".plus(parts.joinToString("/"))
}