package uk.co.ben_gibson.open.in.git.host;

import com.intellij.ide.plugins.IdeaPluginDescriptor;

/**
 * Represents the plugin.
 */
public class Plugin
{
    private String name;
    private String version;

    /**
     * Constructor.
     *
     * @param name     The name.
     * @param version  The version.
     */
    public Plugin(String name, String version)
    {
        this.name    = name;
        this.version = version;
    }

    /**
     * Create from a plugin descriptor.
     *
     * @param pluginDescriptor A plugin descriptor.
     */
    public Plugin(IdeaPluginDescriptor pluginDescriptor)
    {
        this.name    = pluginDescriptor.getName();
        this.version = pluginDescriptor.getVersion();
    }

    @Override
    public String toString() {
        return this.getName().concat("(").concat(this.getVersion()).concat(")");
    }

    /**
     * Get the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the version number.
     */
    public String getVersion()
    {
        return version;
    }
}
