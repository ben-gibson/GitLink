package uk.co.ben_gibson.open.in.git.host.Logger.Handlers;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import uk.co.ben_gibson.open.in.git.host.Logger.LogMessage;
import uk.co.ben_gibson.open.in.git.host.Plugin;

/**
 * Logs messages to the event log window.
 */
public class EventLogHandler implements LogHandler
{
    private Plugin plugin;

    /**
     * Constructor.
     *
     * @param plugin The plugin to log on behalf of.
     */
    public EventLogHandler(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void handle(LogMessage message)
    {
        Notifications.Bus.notify(new Notification(
            this.plugin.getName(),
            this.plugin.toString(),
            message.toString(),
            this.getNotificationTypeForMessage(message)
        ));
    }

    /**
     * Get the notification type for a message.
     *
     * @param message The message to get a notification type from.
     */
    private NotificationType getNotificationTypeForMessage(LogMessage message)
    {
        if (message.isError()) {
            return NotificationType.ERROR;
        } else if (message.isNotice()) {
            return NotificationType.INFORMATION;
        }

        return NotificationType.WARNING; // TODO
    }
}
