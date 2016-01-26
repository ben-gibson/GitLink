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

    private ArrayList<Mapping> mappingList = new ArrayList<>();


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
     * {@inheritDoc}
     */
    public void loadState(Settings state)
    {
        XmlSerializerUtil.copyBean(state, this);
    }
}
