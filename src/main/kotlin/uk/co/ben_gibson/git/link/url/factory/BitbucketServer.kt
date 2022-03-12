package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import uk.co.ben_gibson.git.link.url.template.UrlTemplateConfiguration

@Service
class BitbucketServer : TemplatedUrlFactory(
    UrlTemplateConfiguration(
        "{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/browse/{file:path}/{file:name}?at=refs/heads/{branch}#{line:start}-{line:end}",
        "{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/browse/{file:path}/{file:name}?at={commit}#{line:start}-{line:end}",
        "{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/commits/{commit}"
    )
)