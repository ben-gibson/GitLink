package uk.co.ben_gibson.open.in.git.host.test.RemoteUrlFactory;

import junit.framework.TestCase;
import uk.co.ben_gibson.open.in.git.host.RemoteUrlFactory.RemoteUrlFactory;

public abstract class RemoteUrlFactoryTest extends TestCase
{
    public abstract RemoteUrlFactory remoteUrlFactory();

    public void testCanCreateRemoteUrlToCommit()
    {
        RemoteUrlFactory factory = this.remoteUrlFactory();

        assertTrue(true);


    }

    public void testCanCreateRemoteUrlToFile()
    {
        assertTrue(true);
    }
}
