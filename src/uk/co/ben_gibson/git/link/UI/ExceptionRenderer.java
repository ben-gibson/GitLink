package uk.co.ben_gibson.git.link.UI;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;
import uk.co.ben_gibson.git.link.Plugin;


public class ExceptionRenderer
{
    private Plugin plugin;


    public ExceptionRenderer(Plugin plugin)
    {
        this.plugin = plugin;
    }


    public void render(GitLinkException exception)
    {
        Notifications.Bus.notify(new Notification(
            this.plugin.displayName(),
            this.plugin.toString(),
            exception.getMessage(),
            NotificationType.ERROR
        ));
    }
}
