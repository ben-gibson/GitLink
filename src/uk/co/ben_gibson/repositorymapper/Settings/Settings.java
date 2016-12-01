package uk.co.ben_gibson.repositorymapper.Settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Host.Host;
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
    private boolean forceSSL        = false;
    private Host host               = Host.GIT_HUB;
    private RepositoryProvider repositoryProvider;

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
     * Should we force SSL if the HTTP protocol is not explicitly specified in origin.
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
     * @param forceSSL Should we enforce SSL if the HTTP protocol is not explicitly specified in origin?
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
     * Get the git host.
     *
     * @return Host
     */
    @NotNull
    public Host getHost()
    {
        return host;
    }

    /**
     * Get the repository provider.
     *
     * @deprecated Use getHost instead.
     *
     * @return RepositoryProvider
     */
    public RepositoryProvider getRepositoryProvider()
    {
        return repositoryProvider;
    }

    /**
     * Get the repository provider.
     *
     * @param repositoryProvider The repository provider.
     *
     * @deprecated Use getHost instead.
     */
    public void setRepositoryProvider(RepositoryProvider repositoryProvider)
    {
        this.repositoryProvider = repositoryProvider;
    }

    /**
     * Set the git host.
     *
     * @param host The host.
     */
    public void setHost(@NotNull Host host)
    {
        this.host = host;
    }

    /**
     * {@inheritDoc}
     */
    public void loadState(Settings state)
    {
        XmlSerializerUtil.copyBean(state, this);

        if (state.getRepositoryProvider() != null) {
            switch (state.getRepositoryProvider()) {
                case GIT_HUB:
                    this.setHost(Host.GIT_HUB);
                    break;
                case GITLAB:
                    this.setHost(Host.GITLAB);
                    break;
                case GITBLIT:
                    this.setHost(Host.GITBLIT);
                    break;
                case BITBUCKET:
                    this.setHost(Host.BITBUCKET);
                    break;
                case STASH:
                    this.setHost(Host.STASH);
                    break;
            }

            this.setRepositoryProvider(null);
        }
    }
}
