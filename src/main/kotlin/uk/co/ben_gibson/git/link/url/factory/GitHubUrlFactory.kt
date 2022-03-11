package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import uk.co.ben_gibson.git.link.url.template.UrlTemplateConfiguration

@Service
class GitHubUrlFactory : TemplatedUrlFactory(
    UrlTemplateConfiguration(
        "{remote:url}/blob/{branch}/{file:path}/{file:name}#L{line:start}-L{line:end}",
        "{remote:url}/blob/{commit}/{file:path}/{file:name}#L{line:start}-L{line:end}",
        "{remote:url}/commit/{commit}"
    )
)