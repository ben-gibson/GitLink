package uk.co.ben_gibson.git.link.test;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import junit.framework.TestCase;
import uk.co.ben_gibson.git.link.Plugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PluginTest extends TestCase
{
    public void testToString()
    {
        Plugin plugin = new Plugin(this.mockPluginDescription("foo", "v2.2", "https://example.com"));

        assertEquals(plugin.toString(), "foo(v2.2)");
    }

    public void testReturnsVersion()
    {
        Plugin plugin = new Plugin(this.mockPluginDescription("foo", "v2.2", "https://example.com"));

        assertEquals(plugin.version(), "v2.2");
    }

    public void testReturnsDisplayName()
    {
        Plugin plugin = new Plugin(this.mockPluginDescription("foo", "v2.2", "https://example.com"));

        assertEquals(plugin.displayName(), "foo");
    }

    public void testIssueTracker()
    {
        Plugin plugin = new Plugin(this.mockPluginDescription("foo", "v2.2", "https://example.com"));

        assertEquals(plugin.issueTracker(), "https://example.com/issues");
    }

    private IdeaPluginDescriptor mockPluginDescription(String name, String version, String vendorUrl)
    {
        IdeaPluginDescriptor pluginDescriptor = mock(IdeaPluginDescriptor.class);

        when(pluginDescriptor.getName()).thenReturn(name);
        when(pluginDescriptor.getVersion()).thenReturn(version);
        when(pluginDescriptor.getVendorUrl()).thenReturn(vendorUrl);

        return pluginDescriptor;
    }
}
