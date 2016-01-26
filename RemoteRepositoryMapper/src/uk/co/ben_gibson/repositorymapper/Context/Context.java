package uk.co.ben_gibson.repositorymapper.Context;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a context that can be opened in a remote repository.
 */
public class Context
{
    private String project;
    private String repository;
    private String path;
    private Integer caretLinePosition;


    /**
     * Constructor.
     *
     * @param project           The project.
     * @param repository        The repository.
     * @param path              The path.
     * @param caretLinePosition The line position of the caret.
     */
    public Context(String project, String repository, String path, Integer caretLinePosition)
    {
        this.project       = project;
        this.repository    = repository;
        this.path          = path;
        this.caretLinePosition = caretLinePosition;
    }


    /**
     * Get the project.
     *
     * @return String
     */
    public String getProject()
    {
        return this.project;
    }


    /**
     * Get the repository.
     *
     * @return String
     */
    public String getRepository()
    {
        return this.repository;
    }


    /**
     * Get the path.
     *
     * @return String
     */
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
