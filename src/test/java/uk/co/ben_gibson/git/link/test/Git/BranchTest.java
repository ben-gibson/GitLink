package uk.co.ben_gibson.git.link.test.Git;

import junit.framework.TestCase;
import uk.co.ben_gibson.git.link.Git.Branch;

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
