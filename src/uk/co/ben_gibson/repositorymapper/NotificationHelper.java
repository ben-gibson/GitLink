package uk.co.ben_gibson.repositorymapper;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * Helper for sending notifications.
 */
public class NotificationHelper
{

    /**
     * Error notification.
     */
    public void errorNotification(String message)
    {
        Notification notification = new Notification(
            "RequiredPlugins",
            "Remote Repository Mapper",
            message,
            NotificationType.ERROR
        );

        Notifications.Bus.notify(notification);
    }
}
