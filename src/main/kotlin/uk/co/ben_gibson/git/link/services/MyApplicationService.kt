package uk.co.ben_gibson.git.link.services

import com.intellij.openapi.components.Service
import uk.co.ben_gibson.git.link.GitLinkBundle

@Service
class MyApplicationService {

    init {
        println(GitLinkBundle.message("applicationService"))
    }
}
