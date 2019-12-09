package uk.co.ben_gibson.git.link.UI;

public class LineSelection
{
    private int start;
    private int end;


    public LineSelection(final int start)
    {
        this.start = start;
        this.end   = start;
    }


    public LineSelection(final int start, final int end)
    {
        this.start = start;
        this.end   = end;
    }


    public int start()
    {
        return start;
    }


    public int end()
    {
        return end;
    }


    public boolean isSingleLineSelection()
    {
        return (start == end);
    }


    public boolean isMultiLineSelection()
    {
        return !isSingleLineSelection();
    }
}
