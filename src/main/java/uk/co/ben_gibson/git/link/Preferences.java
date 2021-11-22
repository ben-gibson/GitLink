package uk.co.ben_gibson.git.link;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
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

public class Preferences implements PersistentStateComponent<Preferences>
{
    public boolean      enabled                         = true;
    public String       remoteHostId                    = RemoteHost.GIT_HUB.name();
    public List<String> enabledModifiers                = new ArrayList<>();
    public String       defaultBranchName               = "master";
    public String       remoteName                      = "origin";
    public String       customFileUrlAtCommitTemplate   = "";
    public String       customFileUrlOnBranchTemplate   = "";
    public String       customCommitUrlTemplate         = "";
    public boolean      shouldCheckCommitExistsOnRemote = true;


    public boolean isEnabled()
    {
        return this.enabled;
    }


    @NotNull
    public RemoteHost getRemoteHost()
    {
        return RemoteHost.valueOf(this.remoteHostId);
    }


    @NotNull
    public Branch getDefaultBranch()
    {
        return new Branch(this.defaultBranchName);
    }

    public boolean shouldCheckCommitExistsOnRemote()
    {
        return shouldCheckCommitExistsOnRemote;
    }


    public boolean isModifierEnabled(UrlModifier modifier)
    {
        return this.enabledModifiers.contains(modifier.getClass().getName());
    }


    public void enableModifier(UrlModifier modifier)
    {
        this.enabledModifiers.add(modifier.getClass().getName());
    }


    public void disableModifier(UrlModifier modifier)
    {
        this.enabledModifiers.remove(modifier.getClass().getName());
    }


    public void loadState(@NotNull Preferences state)
    {
        XmlSerializerUtil.copyBean(state, this);
    }


    public static Preferences getInstance(Project project)
    {
        return project.getService(Preferences.class);
    }


    public Preferences getState()
    {
        return this;
    }
}
