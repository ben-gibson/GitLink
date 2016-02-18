package uk.co.ben_gibson.repositorymapper.Settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.RepositoryProvider;

@State(name = "SaveActionSettings",
    storages = {@Storage(id = "default", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/settings.xml")}
)

/**
 * Persistent settings.
 */
public class Settings implements PersistentStateComponent<Settings>
{
    @NotNull
    private Boolean copyToClipboard = false;

    @NotNull
    private RepositoryProvider repositoryProvider = RepositoryProvider.GIT_HUB;


    /**
     * {@inheritDoc}
     *
     * Self maintaining state.
     */
    public Settings getState()
    {
        return this;
    }


    /**
     * Should the results be copied to the clipboard.
     *
     * @return Boolean
     */
    @NotNull
    public Boolean getCopyToClipboard()
    {
        return this.copyToClipboard;
    }


    /**
     * Set copy to clip board preference.
     *
     * getCopyToClipboard  Should the results be copied to the clipboard?
     */
    public void setCopyToClipboard(@NotNull Boolean copyToClipboard)
    {
        this.copyToClipboard = copyToClipboard;
    }


    /**
     * Get the repository provider.
     *
     * @return RepositoryProvider
     */
    @NotNull
    public RepositoryProvider getRepositoryProvider()
    {
        return repositoryProvider;
    }


    /**
     * Set the repository provider.
     *
     * @param repositoryProvider The repository provider.
     */
    public void setRepositoryProvider(@NotNull RepositoryProvider repositoryProvider)
    {
        this.repositoryProvider = repositoryProvider;
    }


    /**
     * {@inheritDoc}
     */
    public void loadState(Settings state)
    {
        XmlSerializerUtil.copyBean(state, this);
    }
}
