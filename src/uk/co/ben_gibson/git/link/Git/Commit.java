package uk.co.ben_gibson.git.link.Git;

import org.jetbrains.annotations.NotNull;


public class Commit
{
    private String hash;

    public Commit(@NotNull String hash)
    {
        this.hash = hash;
    }


    @NotNull
    public String shortHash()
    {
        return this.hash.substring(0, 6);
    }


    @NotNull
    public String hash()
    {
        return this.hash;
    }
}
