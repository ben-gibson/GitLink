package uk.co.ben_gibson.git.link;

import com.intellij.ide.plugins.IdeaPluginDescriptor;

/**
 * Represents the plugin.
 */
public class Plugin
{
    private IdeaPluginDescriptor pluginDescriptor;

    public Plugin(IdeaPluginDescriptor pluginDescriptor)
    {
        this.pluginDescriptor = pluginDescriptor;
    }

    public String toString() {
        return String.format("%s(%s)", this.displayName(), this.version());
    }

    public String displayName()
    {
        return this.pluginDescriptor.getName();
    }

    public String version()
    {
        return this.pluginDescriptor.getVersion();
    }

    public String issueTracker()
    {
        return this.pluginDescriptor.getVendorUrl().concat("/issues");
    }
}
