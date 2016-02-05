package uk.co.ben_gibson.repositorymapper.Context;

import com.intellij.openapi.vfs.VirtualFile;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents some context that can be opened in a remote repository.
 */
public class Context
{
    @NotNull
    private GitRepository repository;
    @NotNull
    private VirtualFile file;
    @Nullable
    private Integer caretLinePosition;


    /**
     * Constructor.
     *
     * @param repository        The repository.
     * @param file              The file.
     * @param caretLinePosition The line position of the caret.
     */
    public Context(
        @NotNull GitRepository repository,
        @NotNull VirtualFile file,
        @Nullable Integer caretLinePosition
    )
    {
        this.repository        = repository;
        this.file              = file;
        this.caretLinePosition = caretLinePosition;
    }


    /**
     * Get the file.
     *
     * @return VirtualFile
     */
    @NotNull
    public VirtualFile getFile()
    {
        return this.file;
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
     * Get the repository.
     *
     * @return GitRepository
     */
    @NotNull
    public GitRepository getRepository()
    {
        return repository;
    }


    /**
     * Get the file path relative to the repository.
     *
     * @return String
     */
    public String getRepositoryRelativeFilePath()
    {
        return this.file.getPath().substring(this.repository.getRoot().getPath().length());
    }
}