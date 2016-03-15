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
    private boolean copyToClipboard = false;

    private boolean forceSSL = false;

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
     * Should we force SSL if the HTTP protocol is not used in origin.
     *
     * @return boolean
     */
    public boolean getForceSSL()
    {
        return this.forceSSL;
    }


    /**
     * Set the force ssl preference.
     *
     * @param forceSSL  Should we enforce SSL if the HTTP protocol is not used in origin?
     */
    public void setForceSSL(boolean forceSSL)
    {
        this.forceSSL = forceSSL;
    }


    /**
     * Should the results be copied to the clipboard.
     *
     * @return boolean
     */
    public boolean getCopyToClipboard()
    {
        return this.copyToClipboard;
    }


    /**
     * Set copy to clip board preference.
     *
     * @param copyToClipboard  Should the results be copied to the clipboard?
     */
    public void setCopyToClipboard(boolean copyToClipboard)
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
