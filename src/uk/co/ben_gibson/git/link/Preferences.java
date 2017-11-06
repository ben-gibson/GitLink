package uk.co.ben_gibson.git.link;

import uk.co.ben_gibson.git.link.Git.Branch;
import uk.co.ben_gibson.git.link.Git.RemoteHost;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import uk.co.ben_gibson.git.link.Url.Modifier.UrlModifier;
import java.util.ArrayList;
import java.util.List;

@State(name = "Preferences",
    storages = {@Storage("GitLinkConfig.xml")}
)

/*
 * Plugin preferences - Getters and Setters required by PersistentStateComponent
 */
public class Preferences implements PersistentStateComponent<Preferences>
{
    private RemoteHost remoteHost          = RemoteHost.GIT_HUB;
    private boolean enableVerboseEventLog  = false;
    private List<String> enabledModifiers  = new ArrayList<>();
    private Branch defaultBranch           = new Branch("master");
    private String customFileUrlTemplate   = "";
    private String customCommitUrlTemplate = "";

    public boolean isModifierEnabled(UrlModifier modifier)
    {
        return this.enabledModifiers.contains(modifier.getClass().getName());
    }

    public List<String> getEnabledModifiers()
    {
        return this.enabledModifiers;
    }

    public void setEnabledModifiers(List<String> enabledModifiers)
    {
        this.enabledModifiers = enabledModifiers;
    }

    public void enableModifier(UrlModifier modifier)
    {
        this.enabledModifiers.add(modifier.getClass().getName());
    }

    public void disableModifier(UrlModifier modifier)
    {
        this.enabledModifiers.remove(modifier.getClass().getName());
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

    public void loadState(Preferences state)
    {
        XmlSerializerUtil.copyBean(state, this);
    }

    public Branch getDefaultBranch()
    {
        return this.defaultBranch;
    }

    public void setDefaultBranch(Branch defaultBranch)
    {
        this.defaultBranch = defaultBranch;
    }

    public String getCustomFileUrlTemplate()
    {
        return customFileUrlTemplate;
    }

    public void setCustomFileUrlTemplate(String customFileUrlTemplate)
    {
        this.customFileUrlTemplate = customFileUrlTemplate;
    }

    public String getCustomCommitUrlTemplate()
    {
        return customCommitUrlTemplate;
    }

    public void setCustomCommitUrlTemplate(String customCommitUrlTemplate)
    {
        this.customCommitUrlTemplate = customCommitUrlTemplate;
    }

    public Preferences getState()
    {
        return this;
    }
}
