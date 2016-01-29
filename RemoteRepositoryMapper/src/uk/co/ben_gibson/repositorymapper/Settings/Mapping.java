package uk.co.ben_gibson.repositorymapper.Settings;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a local => remote mapping.
 */
public class Mapping
{

    private String baseDirectoryPath;

    private String project;

    private String repository;

    /**
     * Support serialisation.
     */
    public Mapping()
    {

    }


    /**
     * Constructor.
     *
     * @param baseDirectoryPath  The base directory path that maps to Stash.
     * @param project            The Stash project that relates to the base directory.
     * @param repository         The Stash repository that relates to the base directory.
     */
    public Mapping(@NotNull String baseDirectoryPath, @NotNull String project, @NotNull String repository)
    {
        this.baseDirectoryPath = baseDirectoryPath;
        this.project           = project;
        this.repository        = repository;
    }


    /**
     * Get the base directory path.
     *
     * @return String
     */
    @NotNull
    public String getBaseDirectoryPath()
    {
        return this.baseDirectoryPath;
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
     * Set the base directory path.
     *
     * Vital for persistence!
     *
     * @param baseDirectoryPath The base directory path.
     */
    public void setBaseDirectoryPath(@NotNull String baseDirectoryPath)
    {
        this.baseDirectoryPath = baseDirectoryPath;
    }


    /**
     * Set the project.
     *
     * Vital for persistence!
     *
     * @param project The project.
     */
    public void setProject(@NotNull String project)
    {
        this.project = project;
    }


    /**
     * Set the repository.
     *
     * Vital for persistence!
     *
     * @param repository The repository.
     */
    public void setRepository(@NotNull String repository)
    {
        this.repository = repository;
    }
}