package uk.co.ben_gibson.open.in.git.host;

import com.intellij.ide.plugins.IdeaPluginDescriptor;

/**
 * Represents the plugin.
 */
public class Plugin
{
    private String name;
    private String version;

    public Plugin(String name, String version)
    {
        this.name    = name;
        this.version = version;
    }

    public Plugin(IdeaPluginDescriptor pluginDescriptor)
    {
        this(pluginDescriptor.getName(), pluginDescriptor.getVersion());
    }

    public String toString() {
        return String.format("%s(%s)", this.displayName(), this.version());
    }

    public String displayName()
    {
        return name;
    }

    public String version()
    {
        return version;
    }
}
