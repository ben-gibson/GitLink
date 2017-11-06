package uk.co.ben_gibson.git.link.Logger.Handlers;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import uk.co.ben_gibson.git.link.Logger.LogMessage;
import uk.co.ben_gibson.git.link.Plugin;

/**
 * Logs messages to the event log window.
 */
public class EventLogHandler implements LogHandler
{
    private Plugin plugin;
    private boolean verbose;

    public EventLogHandler(Plugin plugin, boolean verbose)
    {
        this.plugin  = plugin;
        this.verbose = verbose;
    }

    public void handle(LogMessage message)
    {
        Notifications.Bus.notify(new Notification(
            this.plugin.displayName(),
            this.plugin.toString(),
            message.toString(),
            this.getNotificationTypeForMessage(message)
        ));
    }

    private NotificationType getNotificationTypeForMessage(LogMessage message)
    {
        if (message.error()) {
            return NotificationType.ERROR;
        } else if (message.notice()) {
            return NotificationType.INFORMATION;
        }

        return NotificationType.WARNING;
    }

    public boolean handles(LogMessage message)
    {
        return !message.notice() || (message.notice() && this.verbose);
    }
}
