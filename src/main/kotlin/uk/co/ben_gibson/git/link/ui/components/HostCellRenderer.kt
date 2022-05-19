package uk.co.ben_gibson.git.link.ui.components

import com.intellij.ui.SimpleListCellRenderer
import uk.co.ben_gibson.git.link.git.Host
import javax.swing.JList

class HostCellRenderer : SimpleListCellRenderer<Host>() {
    override fun customize(list: JList<out Host>, value: Host?, index: Int, selected: Boolean, hasFocus: Boolean) {
        text = value?.displayName ?: ""
        icon = value?.icon
    }
}