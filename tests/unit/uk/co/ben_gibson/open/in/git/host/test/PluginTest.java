package uk.co.ben_gibson.open.in.git.host.test;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import junit.framework.TestCase;
import uk.co.ben_gibson.open.in.git.host.Plugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PluginTest extends TestCase
{
    public void testToString()
    {
        Plugin plugin = new Plugin("foo", "v2.2");

        assertEquals(plugin.toString(), "foo(v2.2)");
    }

    public void testReturnsVersion()
    {
        Plugin plugin = new Plugin("foo", "v2.2");

        assertEquals(plugin.version(), "v2.2");
    }

    public void testReturnsDisplayName()
    {
        Plugin plugin = new Plugin("foo", "v2.2");

        assertEquals(plugin.displayName(), "foo");
    }

    public void testCanBeCreatedFromIdeaPluginDescriptor()
    {

        IdeaPluginDescriptor pluginDescriptor = mock(IdeaPluginDescriptor.class);

        when(pluginDescriptor.getName()).thenReturn("bar");
        when(pluginDescriptor.getVersion()).thenReturn("v1.0.2");

        Plugin plugin = new Plugin(pluginDescriptor);

        assertEquals(plugin.displayName(), "bar");
        assertEquals(plugin.version(), "v1.0.2");
    }
}
