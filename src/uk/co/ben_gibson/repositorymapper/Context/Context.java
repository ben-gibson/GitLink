package uk.co.ben_gibson.repositorymapper.Context;

import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitBranch;
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
    private GitBranch branch;
    @NotNull
    private VirtualFile file;
    @Nullable
    private Integer caretLinePosition;


    /**
     * Constructor.
     *
     * @param repository        The repository.
     * @param branch            The branch if we have one.
     * @param file              The file.
     * @param caretLinePosition The line position of the caret.
     */
    public Context(
        @NotNull GitRepository repository,
        @NotNull GitBranch branch,
        @NotNull VirtualFile file,
        @Nullable Integer caretLinePosition
    )
    {
        this.repository        = repository;
        this.branch            = branch;
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
     * Get the branch.
     *
     * @return GitBranch
     */
    @NotNull
    public GitBranch getBranch()
    {
        return this.branch;
    }
}