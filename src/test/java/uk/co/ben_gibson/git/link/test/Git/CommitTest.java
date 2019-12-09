package uk.co.ben_gibson.git.link.test.Git;

import junit.framework.TestCase;
import uk.co.ben_gibson.git.link.Git.Commit;

public class CommitTest extends TestCase
{
    public void testCanGetHash()
    {
        Commit commit = new Commit("bd7ab0f04151e7409d77caa296617f97352f36d3");

        assertSame("bd7ab0f04151e7409d77caa296617f97352f36d3", commit.hash());
    }
}
