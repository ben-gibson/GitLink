package uk.co.ben_gibson.git.link.test;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import junit.framework.TestCase;
import uk.co.ben_gibson.git.link.Plugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PluginTest extends TestCase
{
    public void testToString() {
        Plugin plugin = new Plugin(mockPluginDescription());

        assertEquals(plugin.toString(), "foo(v2.2)");
    }

    public void testReturnsVersion() {
        Plugin plugin = new Plugin(mockPluginDescription());

        assertEquals(plugin.version(), "v2.2");
    }

    public void testReturnsDisplayName() {
        Plugin plugin = new Plugin(mockPluginDescription());

        assertEquals(plugin.displayName(), "foo");
    }

    public void testIssueTracker() {
        Plugin plugin = new Plugin(mockPluginDescription());

        assertEquals(plugin.issueTracker(), "https://example.com/issues");
    }

    private IdeaPluginDescriptor mockPluginDescription() {
        IdeaPluginDescriptor pluginDescriptor = mock(IdeaPluginDescriptor.class);

        when(pluginDescriptor.getName()).thenReturn("foo");
        when(pluginDescriptor.getVersion()).thenReturn("v2.2");
        when(pluginDescriptor.getVendorUrl()).thenReturn("https://example.com");

        return pluginDescriptor;
    }
}
