package uk.co.ben_gibson.git.link.url.factory

import com.intellij.openapi.components.Service
import uk.co.ben_gibson.git.link.url.template.UrlTemplateConfiguration

@Service
class Azure : TemplatedUrlFactory(
    UrlTemplateConfiguration(
        "{remote:url}?version=GB{branch}&path=/{file:path}{file:name}&line={line:start}&lineEnd={line:end}&lineStartColumn=1&lineEndColumn=1",
        "{remote:url}?version=GC{commit}&path=/{file:path}{file:name}&line={line:start}&lineEnd={line:end}&lineStartColumn=1&lineEndColumn=1",
        "{remote:url}/commit/{commit}"
    )
)