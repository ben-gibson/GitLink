package uk.co.ben_gibson.git.link;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.serviceContainer.NonInjectable;
import org.jetbrains.annotations.NotNull;

public class Plugin
{
    private final IdeaPluginDescriptor pluginDescriptor;

    public Plugin() {
        this.pluginDescriptor = PluginManager.getPlugin(PluginId.getId("uk.co.ben-gibson.remote.repository.mapper"));
    }

    @NonInjectable
    public Plugin(IdeaPluginDescriptor descriptor) {
        this.pluginDescriptor = descriptor;
    }

    @NotNull
    public String toString()
    {
        return String.format("%s(%s)", this.displayName(), this.version());
    }

    @NotNull
    public String displayName() {
        return this.pluginDescriptor.getName();
    }

    @NotNull
    public String version() {
        return pluginDescriptor.getVersion();
    }

    @NotNull
    public String issueTracker() {
        return pluginDescriptor.getVendorUrl().concat("/issues");
    }

    @NotNull
    public String customHostDocs() {
        return pluginDescriptor.getVendorUrl().concat("/wiki/Custom-Host");
    }
}
