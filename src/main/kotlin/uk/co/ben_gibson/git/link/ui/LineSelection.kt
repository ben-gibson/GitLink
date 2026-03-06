package uk.co.ben_gibson.git.link.ui

data class LineSelection(val start: Int, val end: Int) {
    init {
        require(start > 0) { "Start line must be positive, got $start" }
        require(end > 0) { "End line must be positive, got $end" }
        require(start <= end) { "Start line ($start) must be <= end line ($end)" }
    }

    constructor(line: Int) : this(line, line)

    val lineCount: Int
        get() = end - start + 1
}