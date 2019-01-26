package uk.co.ben_gibson.git.link.test.UI;

import junit.framework.TestCase;
import uk.co.ben_gibson.git.link.UI.LineSelection;

public class LineSelectionTest extends TestCase
{
    public void testCanDetermineIfSingleLine()
    {
        LineSelection lineSelection = new LineSelection(10);

        assertTrue(lineSelection.isSingleLineSelection());
        assertFalse(lineSelection.isMultiLineSelection());
    }


    public void testCanDetermineIfMultiLine()
    {
        LineSelection lineSelection = new LineSelection(10, 20);

        assertTrue(lineSelection.isMultiLineSelection());
        assertFalse(lineSelection.isSingleLineSelection());
    }


    public void testReturnsExpectedStart()
    {
        LineSelection lineSelection = new LineSelection(10, 20);

        assertSame(lineSelection.start(), 10);
    }


    public void testReturnsExpectedEnd()
    {
        LineSelection lineSelection = new LineSelection(10, 20);

        assertSame(lineSelection.end(), 20);
    }
}
