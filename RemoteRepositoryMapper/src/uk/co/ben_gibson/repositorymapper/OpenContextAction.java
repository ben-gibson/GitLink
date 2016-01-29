package uk.co.ben_gibson.repositorymapper;

import com.intellij.openapi.vcs.VcsException;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.repositorymapper.Context.Context;
import uk.co.ben_gibson.repositorymapper.Context.ContextFactory;
import uk.co.ben_gibson.repositorymapper.Settings.Settings;
import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import uk.co.ben_gibson.repositorymapper.UrlFactory.UrlFactoryProvider;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Opens the current context in a remote repository.
 */
public class OpenContextAction extends AnAction
{

    /**
     * Open the current context.
     *
     * @param event The event.
     */
    public void actionPerformed(AnActionEvent event)
    {

        Project project = event.getProject();

        if (project == null) {
            return;
        }

        Settings settings = this.getProjectSettings(project);

        try {

            Context context = this.getContextFactory().create(project, settings);

            UrlFactoryProvider urlFactoryProvider = ServiceManager.getService(UrlFactoryProvider.class);

            URL url = urlFactoryProvider.getUrlFactoryForProvider(settings.getRepositoryProvider()).getUrlFromContext(context);

            if (settings.copyToClipboard()) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(url.toString()), null);
            }

            // Open in the default browser
            BrowserLauncher.getInstance().browse(url.toURI());

        } catch (MalformedURLException | URISyntaxException | VcsException e) {
            e.printStackTrace();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(AnActionEvent event)
    {
        if (event.getProject() == null) {
            return;
        }

        Settings settings = this.getProjectSettings(event.getProject());

        Context context = null;

        try {
            context = this.getContextFactory().create(event.getProject(), settings);
        } catch (MalformedURLException | VcsException e) {
            e.printStackTrace();
        }

        event.getPresentation().setEnabledAndVisible((context != null));
    }



    /**
     * Get the project settings.
     *
     * @param project  The project to fetch settings for.
     *
     * @return Settings
     */
    @NotNull
    private Settings getProjectSettings(@NotNull Project project)
    {
        return ServiceManager.getService(project, Settings.class);
    }


    /**
     * Get the context factory.
     *
     * @return ContextFactory
     */
    @NotNull
    private ContextFactory getContextFactory()
    {
        return ServiceManager.getService(ContextFactory.class);
    }
}