package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import uk.co.ben_gibson.git.link.url.template.UrlTemplateConfiguration

@Service
class BitbucketCloud : TemplatedUrlFactory(
    UrlTemplateConfiguration(
        "{remote:url}/src/HEAD/{file:path}/{file:name}?at={branch}#lines-{line:start}:{line:end}",
        "{remote:url}/src/{commit}/{file:path}/{file:name}#lines-{line:start}:{line:end}",
        "{remote:url}/commits/{commit}"
    )
)