package uk.co.ben_gibson.git.link.ui.components

import com.intellij.openapi.components.service
import com.intellij.ui.CollectionComboBoxModel
import uk.co.ben_gibson.git.link.git.Host
import uk.co.ben_gibson.git.link.git.HostsProvider

object HostComboBoxModelProvider {
    fun provide() : CollectionComboBoxModel<Host> {
        return CollectionComboBoxModel(service<HostsProvider>().provide().toList());
    }
}