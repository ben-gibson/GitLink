package uk.co.ben_gibson.repositorymapper.Settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;
import java.util.ArrayList;

@State(name = "SaveActionSettings",
    storages = {@Storage(id = "default", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/settings.xml")}
)

/**
 * Persists the settings state.
 */
public class Settings implements PersistentStateComponent<Settings>
{

    public enum RepositoryProvider
    {
        BIT_BUCKET,
        GIT_HUB,
    }

    private ArrayList<Mapping> mappingList = new ArrayList<>();

    private Host host;

    private Boolean copyToClipboard = false;

    private RepositoryProvider repositoryProvider;


    /**
     * {@inheritDoc}
     *
     * Maintains its own state so returns itself.
     */
    public Settings getState()
    {
        return this;
    }


    /**
     * Get mapping collection.
     *
     * Vital for persistence!
     *
     * @return ArrayList<Mapping>
     */
    public ArrayList<Mapping> getMappingList()
    {
        return this.mappingList;
    }


    /**
     * Set mapping collection.
     *
     * Vital for persistence!
     *
     * @param mappingList The mapping list to set.
     */
    public void setMappingList(ArrayList<Mapping> mappingList)
    {
        this.mappingList = mappingList;
    }


    /**
     * Get the host.
     *
     * @return Host
     */
    public Host getHost()
    {
        return host;
    }


    /**
     * Set the host.
     *
     * host  The host.
     */
    public void setHost(Host host)
    {
        this.host = host;
    }


    /**
     * Should we copy the result to the clipboard.
     *
     * @return Boolean
     */
    public Boolean shouldCopyToClipboard()
    {
        return copyToClipboard;
    }


    /**
     * Set copy to clip board preference.
     *
     * copyToClipboard  Should we copy the result to the clipboard?
     */
    public void setCopyToClipboard(Boolean copyToClipboard)
    {
        this.copyToClipboard = copyToClipboard;
    }


    /**
     * Get the repository provider.
     *
     * @return RepositoryProvider
     */
    public RepositoryProvider getRepositoryProvider()
    {
        return repositoryProvider;
    }


    /**
     * Set the repository provider.
     *
     * @param repositoryProvider  The repository provider.
     */
    public void setRepositoryProvider(RepositoryProvider repositoryProvider)
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
