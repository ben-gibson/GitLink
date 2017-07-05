package uk.co.ben_gibson.repositorymapper.Logger.Handler;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import uk.co.ben_gibson.repositorymapper.Logger.LogMessage;

/**
 * Logs messages to the Intellij event log.
 */
public class EventLogHandler implements LogHandler
{
    @Override
    public void handle(LogMessage message)
    {
        Notifications.Bus.notify(new Notification("Open In Git Host", "Foo", message.toString(), NotificationType.ERROR));
    }
}
