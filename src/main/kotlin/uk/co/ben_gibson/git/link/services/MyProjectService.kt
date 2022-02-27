package uk.co.ben_gibson.git.link.services

import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
