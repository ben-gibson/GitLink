package uk.co.ben_gibson.git.link.UI;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import uk.co.ben_gibson.git.link.Exception.GitLinkException;
import uk.co.ben_gibson.git.link.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Renders exceptions in the UI in a end user friendly way.
 */
public class ExceptionRenderer
{
    private Plugin plugin;

    private static final Map<String, String> errorMap;
    static
    {
        errorMap = new HashMap<>();

        // git
        errorMap.put("0x01000004", "Git repository not found, make sure you have registered your version control root: Preferences → Version Control");
        errorMap.put("0x01000001", "Could not find the remote, check your preferences!");

        // URL

        errorMap.put("0x02000003", "Could not create valid URL!");
    }


    public ExceptionRenderer(Plugin plugin)
    {
        this.plugin = plugin;
    }


    public void render(GitLinkException exception)
    {
        String message;
        String code = String.format("0x%08X", exception.code());

        if (errorMap.containsKey(code)) {
            message = errorMap.get(code);
        } else {
            message = String.format("An unknown error occurred. %s", code);
        }

        Notifications.Bus.notify(new Notification(
            this.plugin.displayName(),
            this.plugin.toString(),
            message,
            NotificationType.ERROR
        ));
    }
}
