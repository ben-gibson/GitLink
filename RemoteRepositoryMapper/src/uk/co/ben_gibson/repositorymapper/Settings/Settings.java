package uk.co.ben_gibson.repositorymapper.Settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.RepositoryProvider.RepositoryProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

@State(name = "SaveActionSettings",
    storages = {@Storage(id = "default", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/settings.xml")}
)

/**
 * Persistent settings.
 */
public class Settings implements PersistentStateComponent<Settings>
{

    private ArrayList<Mapping> mappingList = new ArrayList<>();

    @NotNull
    private String host = "https://github.com";

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
     * Get mapping collection.
     *
     * @return ArrayList<Mapping>
     */
    @NotNull
    public ArrayList<Mapping> getMappingList()
    {
        return this.mappingList;
    }


    /**
     * Set mapping collection.
     *
     * @param mappingList The mapping list to set.
     */
    public void setMappingList(@NotNull ArrayList<Mapping> mappingList)
    {
        this.mappingList = mappingList;
    }


    /**
     * Get host.
     *
     *
     * @return String
     */
    @NotNull
    public String getHost()
    {
        return this.host;
    }


    /**
     * Get host.
     *
     * I would prefer host to be a URL by default however I don't think it's possible for these serializable classes?
     *
     * @return URL
     */
    @NotNull
    public URL getHostAsURL() throws MalformedURLException
    {
        return new URL(this.host);
    }


    /**
     * Set the host.
     *
     * host  The host.
     */
    public void setHost(@NotNull String host)
    {
        this.host = host;
    }


    /**
     * Should we copy the result to the clipboard.
     *
     * @return Boolean
     */
    @NotNull
    public Boolean copyToClipboard()
    {
        return copyToClipboard;
    }


    /**
     * Set copy to clip board preference.
     *
     * copyToClipboard  Should we copy the result to the clipboard?
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
     * @param repositoryProvider  The repository provider.
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
