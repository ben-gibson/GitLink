package uk.co.ben_gibson.git.link;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NotNull;


public class Plugin
{
    private IdeaPluginDescriptor pluginDescriptor;


    public Plugin(IdeaPluginDescriptor pluginDescriptor)
    {
        this.pluginDescriptor = pluginDescriptor;
    }


    public Plugin()
    {
        this.pluginDescriptor = PluginManager.getPlugin(PluginId.getId("uk.co.ben-gibson.remote.repository.mapper"));
    }


    @NotNull
    public String toString()
    {
        return String.format("%s(%s)", this.displayName(), this.version());
    }


    @NotNull
    public String displayName()
    {
        return this.pluginDescriptor.getName();
    }


    @NotNull
    public String version()
    {
        return this.pluginDescriptor.getVersion();
    }


    @NotNull
    public String issueTracker()
    {
        return this.pluginDescriptor.getVendorUrl().concat("/issues");
    }
}
