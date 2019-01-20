package uk.co.ben_gibson.git.link.UI;

import org.jetbrains.annotations.NotNull;

public class LineSelection
{
    private int start;
    private int end;


    public LineSelection(@NotNull int start)
    {
        this.start = start;
        this.end   = start;
    }


    public LineSelection(@NotNull int start, @NotNull int end)
    {
        this.start = start;
        this.end   = end;
    }


    public int start()
    {
        return this.start;
    }


    public int end()
    {
        return this.end;
    }


    public boolean isSingleLineSelection()
    {
        return (this.start == this.end);
    }


    public boolean isMultiLineSelection()
    {
        return !this.isSingleLineSelection();
    }
}
