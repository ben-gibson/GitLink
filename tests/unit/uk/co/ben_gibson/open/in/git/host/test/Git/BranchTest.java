package uk.co.ben_gibson.open.in.git.host.test.Git;

import junit.framework.TestCase;
import uk.co.ben_gibson.open.in.git.host.Git.Branch;

public class BranchTest extends TestCase
{
    public void testCanBeCastToString()
    {
        Branch branch = new Branch("dev");

        assertSame("dev", branch.toString());
    }

    public void testCanCreateMasterBranch()
    {
        Branch branch = Branch.master();

        assertSame("master", branch.toString());
    }
}
