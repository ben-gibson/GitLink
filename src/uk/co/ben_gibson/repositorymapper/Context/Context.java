package uk.co.ben_gibson.repositorymapper.Context;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.BranchNotFoundException;
import uk.co.ben_gibson.repositorymapper.Repository.Exception.RemoteNotFoundException;
import uk.co.ben_gibson.repositorymapper.Repository.Repository;
import uk.co.ben_gibson.repositorymapper.Settings.Settings;

/**
 * A contextual representation of a version controlled file and its current state.
 */
public class Context
{
    @NotNull
    private Repository repository;
    @NotNull
    private VirtualFile file;
    @Nullable
    private String commitHash;
    @Nullable
    private Integer caretLinePosition;
    private Settings settings;

    /**
     * Constructor.
     *  @param repository        The repository the file belongs to.
     * @param file              The file.
     * @param commitHash        The commit hash.
     * @param caretLinePosition The line number.
     * @param settings
     */
    public Context(
            @NotNull Repository repository,
            @NotNull VirtualFile file,
            @Nullable String commitHash,
            @Nullable Integer caretLinePosition,
            @NotNull Settings settings)
    {
        this.repository        = repository;
        this.file              = file;
        this.commitHash        = commitHash;
        this.caretLinePosition = caretLinePosition;
        this.settings = settings;
    }

    /**
     * Get the settings.
     * @return Settings
     */
    public Settings getSettings() {
        return settings;
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
     * Get the commit hash.
     *
     * @return String
     */
    @Nullable
    public String getCommitHash()
    {
        return this.commitHash;
    }

    /**
     * Get the line number
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
     * @return Repository
     */
    @NotNull
    public Repository getRepository()
    {
        return repository;
    }

    /**
     * Get the branch.
     *
     * @return String
     */
    @NotNull
    public String getBranch() throws RemoteNotFoundException
    {
        try {
            return this.repository.getCurrentBranch();
        } catch (BranchNotFoundException e) {
            return this.repository.getDefaultBranch();
        }
    }

    /**
     * Get the file path relative to the repository.
     *
     * @return String
     */
    @NotNull
    public String getFilePathRelativeToRepository()
    {
        return this.file.getPath().substring(this.repository.getRoot().getPath().length());
    }
}