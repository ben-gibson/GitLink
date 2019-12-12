package uk.co.ben_gibson.git.link.Git;

import org.jetbrains.annotations.NotNull;

public class File
{
    private final String path; // A path from the root of the repository.
    private final String name;

    public File(@NotNull String path, @NotNull final String name)
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
        return path.substring(0, (path.length() - name().length()));
    }

    @NotNull
    public String path()
    {
        return path;
    }

    @NotNull
    public String name()
    {
        return name;
    }
}
