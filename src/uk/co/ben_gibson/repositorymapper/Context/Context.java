package uk.co.ben_gibson.repositorymapper.Context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.net.URL;

/**
 * Represents some context that can be opened in a remote repository.
 */
public class Context
{
    private static final String DEFAULT_BRANCH = "master";

    @NotNull
    private URL remoteHost;
    @NotNull
    private String path;
    @NotNull
    private String branch;
    @Nullable
    private Integer caretLinePosition;


    /**
     * Constructor.
     *
     * @param remoteHost        The remote host.
     * @param path              The path of the file we want to view.
     * @param branch            The branch if we have one.
     * @param caretLinePosition The line position of the caret.
     */
    public Context(
        @NotNull URL remoteHost,
        @NotNull String path,
        @Nullable String branch,
        @Nullable Integer caretLinePosition
    )
    {
        this.remoteHost        = remoteHost;
        this.path              = path;
        this.branch            = (branch != null) ? branch : DEFAULT_BRANCH;
        this.caretLinePosition = caretLinePosition;
    }


    /**
     * Get the path.
     *
     * @return String
     */
    @NotNull
    public String getPath()
    {
        return this.path;
    }


    /**
     * Get the caret line position.
     *
     * @return Integer
     */
    @Nullable
    public Integer getCaretLinePosition()
    {
        return this.caretLinePosition;
    }


    /**
     * Get the remote host.
     *
     * @return URL
     */
    @NotNull
    public URL getRemoteHost() {
        return remoteHost;
    }


    /**
     * Get the branch.
     *
     * @return String
     */
    @NotNull
    public String getBranch() {
        return this.branch;
    }
}