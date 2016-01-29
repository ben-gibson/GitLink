package uk.co.ben_gibson.repositorymapper.Context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

/**
 * Represents a context that can be opened in a remote repository.
 */
public class Context
{
    @NotNull
    private URL host;
    @NotNull
    private String project;
    @NotNull
    private String repository;
    @NotNull
    private String path;
    @Nullable
    private Integer caretLinePosition;


    /**
     * Constructor.
     *
     * @param host              The host.
     * @param project           The project.
     * @param repository        The repository.
     * @param path              The path.
     * @param caretLinePosition The line position of the caret.
     */
    public Context(@NotNull URL host, @NotNull String project, @NotNull String repository, @NotNull String path, @Nullable Integer caretLinePosition)
    {
        this.host              = host;
        this.project           = project;
        this.repository        = repository;
        this.path              = path;
        this.caretLinePosition = caretLinePosition;
    }


    /**
     * Get the host.
     *
     * @return URL
     */
    @NotNull
    public URL getHost()
    {
        return host;
    }


    /**
     * Get the project.
     *
     * @return String
     */
    @NotNull
    public String getProject()
    {
        return this.project;
    }


    /**
     * Get the repository.
     *
     * @return String
     */
    @NotNull
    public String getRepository()
    {
        return this.repository;
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
}
