package uk.co.ben_gibson.git.link.Git;

import org.jetbrains.annotations.NotNull;

public class File
{
    private String path; // A path from the root of the repository.
    private String name;

    public File(@NotNull String path, @NotNull String name)
    {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        this.path = path;
        this.name = name;
    }

    @NotNull
    public String directoryPath()
    {
        return this.path.substring(0, (this.path.length() - this.name().length()));
    }

    @NotNull
    public String path()
    {
        return this.path;
    }

    @NotNull
    public String name()
    {
        return this.name;
    }
}
