package uk.co.ben_gibson.git.link.UI;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;
import uk.co.ben_gibson.git.link.Plugin;

public class ExceptionRenderer
{
    private final Plugin plugin;

    public ExceptionRenderer() {
        this.plugin = ApplicationManager.getApplication().getService(Plugin.class);
    }

    public static ExceptionRenderer getInstance() {
        return ApplicationManager.getApplication().getService(ExceptionRenderer.class);
    }

    public void render(@NotNull final GitLinkException exception) {
        Notifications.Bus.notify(new Notification(
            plugin.displayName(),
            plugin.toString(),
            exception.getMessage(),
            NotificationType.ERROR
        ));
    }
}
