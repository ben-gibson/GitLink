package uk.co.ben_gibson.git.link.services

import com.intellij.openapi.project.Project
import uk.co.ben_gibson.git.link.GitLinkBundle

class MyProjectService(project: Project) {

    init {
        println(GitLinkBundle.message("projectService", project.name))
    }
}
