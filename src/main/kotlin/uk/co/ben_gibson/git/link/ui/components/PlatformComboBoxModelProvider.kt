package uk.co.ben_gibson.git.link.ui.components

import com.intellij.openapi.components.service
import com.intellij.ui.CollectionComboBoxModel
import uk.co.ben_gibson.git.link.platform.Platform
import uk.co.ben_gibson.git.link.platform.PlatformRepository

object PlatformComboBoxModelProvider {
    fun provide() : CollectionComboBoxModel<Platform> {
        return CollectionComboBoxModel(service<PlatformRepository>().getAll().toList())
    }
}