package uk.co.ben_gibson.git.link.platform

import uk.co.ben_gibson.git.link.url.template.UrlTemplates
import java.util.UUID

/**
 * A platform defined by the user in the custom platform settings. [Custom] wraps one of these to present it
 * as a [Platform] the rest of the plugin can generate links for, so this is the type to edit and store.
 */
data class CustomPlatform(
    val id: UUID,
    val name: String,
    val domain: Domain,
    val templates: UrlTemplates
)
