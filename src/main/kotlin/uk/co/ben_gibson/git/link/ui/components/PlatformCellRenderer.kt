package uk.co.ben_gibson.git.link.ui.components

import com.intellij.ui.SimpleListCellRenderer
import uk.co.ben_gibson.git.link.platform.Platform
import javax.swing.JList

class PlatformCellRenderer : SimpleListCellRenderer<Platform>() {
    override fun customize(list: JList<out Platform>, value: Platform?, index: Int, selected: Boolean, hasFocus: Boolean) {
        text = value?.name ?: ""
        icon = value?.icon
    }
}