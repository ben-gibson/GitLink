package uk.co.ben_gibson.git.link.Git;

import org.jetbrains.annotations.NotNull;

public class Branch
{
    private String name;

    public Branch(@NotNull String name)
    {
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
        return this.name;
    }

    public Boolean equals(Branch branch)
    {
        return this.toString().equals(branch.toString());
    }
}
