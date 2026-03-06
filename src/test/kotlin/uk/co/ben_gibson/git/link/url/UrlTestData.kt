package uk.co.ben_gibson.git.link.url

import uk.co.ben_gibson.git.link.git.Commit
import uk.co.ben_gibson.git.link.git.File
import uk.co.ben_gibson.git.link.ui.LineSelection

object UrlTestData {
    const val BRANCH_MAIN = "main"
    const val BRANCH_MASTER = "master"

    val COMMIT_FULL = Commit("b032a0707beac9a2f24b1b7d97ee4f7156de182c")

    val FILE_JAVA = File("Foo.java", false, "src", false)

    val DIR_RESOURCES = File("resources", true, "src/foo", false)
    val DIR_ROOT = File("my-project", true, "", true)

    val LINE_SELECTION_RANGE = LineSelection(10, 20)
}

