package uk.co.ben_gibson.git.link.Git;

import org.jetbrains.annotations.NotNull;

public class Branch
{
    private final String name;

    public Branch(@NotNull final String name) {
        this.name = name;
    }

    @NotNull
    public static Branch master()
    {
        return new Branch("master");
    }

    @NotNull
    public String toString()
    {
        return name;
    }
}
