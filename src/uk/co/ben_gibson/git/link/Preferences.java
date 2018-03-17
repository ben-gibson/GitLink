package uk.co.ben_gibson.git.link;

import com.intellij.openapi.components.ServiceManager;
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

/*
 * Plugin preferences - Getters and Setters required by PersistentStateComponent
 */
public class Preferences implements PersistentStateComponent<Preferences>
{
    public RemoteHost remoteHost = RemoteHost.GIT_HUB;
    public List<String> enabledModifiers = new ArrayList<>();
    public Branch defaultBranch = new Branch("master");
    public String remoteName = "origin";
    public String customFileUrlAtCommitTemplate = "";
    public String customFileUrlOnBranchTemplate = "";
    public String customCommitUrlTemplate = "";

    /** @deprecated **/
    private String customFileUrlTemplate = "";


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

        if (!state.customFileUrlTemplate.equals("")) {
            this.customFileUrlOnBranchTemplate = state.customFileUrlTemplate;
            this.customFileUrlTemplate = "";
        }
    }


    public static Preferences getInstance(Project project)
    {
        return ServiceManager.getService(project, Preferences.class);
    }


    public Preferences getState()
    {
        return this;
    }
}
