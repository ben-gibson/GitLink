package uk.co.ben_gibson.open.in.git.host.test.Git;

import com.intellij.vcs.log.Hash;
import com.intellij.vcs.log.VcsFullCommitDetails;
import junit.framework.TestCase;
import uk.co.ben_gibson.open.in.git.host.Git.Commit;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommitTest extends TestCase
{
    public void testCanGetHash()
    {
        VcsFullCommitDetails details = mock(VcsFullCommitDetails.class);
        Hash hash = mock(Hash.class);

        when(details.getId()).thenReturn(hash);
        when(hash.toString()).thenReturn("bd7ab0f04151e7409d77caa296617f97352f36d3");

        Commit commit = new Commit(details);

        assertSame("bd7ab0f04151e7409d77caa296617f97352f36d3", commit.hash());
    }
}
