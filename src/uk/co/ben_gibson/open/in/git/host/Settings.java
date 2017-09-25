package uk.co.ben_gibson.open.in.git.host;

import uk.co.ben_gibson.open.in.git.host.Extension.Extension;
import uk.co.ben_gibson.open.in.git.host.Git.RemoteHost;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;
import java.util.ArrayList;
import java.util.List;

@State(name = "uk.co.ben_gibson.open.in.git.host.Settings",
    storages = {@Storage(id = "default", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/settings.xml")}
)

/*
 * Plugin settings - Getters and Setters required by PersistentStateComponent
 */
public class Settings implements PersistentStateComponent<Settings>
{
    private RemoteHost remoteHost          = RemoteHost.GIT_HUB;
    private boolean enableVerboseEventLog  = false;
    private boolean forceSSL               = false;
    private List<String> enabledExtensions = new ArrayList<>();

    public boolean isExtensionEnabled(Extension extension)
    {
        return this.enabledExtensions.contains(extension.getClass().getName());
    }

    public List<String> getEnabledExtensions()
    {
        return this.enabledExtensions;
    }

    public void setEnabledExtensions(List<String> enabledExtensions)
    {
        this.enabledExtensions = enabledExtensions;
    }

    public boolean getForceSSL()
    {
        return this.forceSSL;
    }

    public void setForceSSL(boolean forceSSL)
    {
        this.forceSSL = forceSSL;
    }

    public boolean getEnableVerboseEventLog()
    {
        return this.enableVerboseEventLog;
    }

    public void setEnableVerboseEventLog(boolean enableVerboseEventLog)
    {
        this.enableVerboseEventLog = enableVerboseEventLog;
    }

    public void setRemoteHost(RemoteHost remoteHost)
    {
        this.remoteHost = remoteHost;
    }

    public RemoteHost getRemoteHost()
    {
        return this.remoteHost;
    }

    public void loadState(Settings state)
    {
        XmlSerializerUtil.copyBean(state, this);
    }

    public Settings getState()
    {
        return this;
    }
}
