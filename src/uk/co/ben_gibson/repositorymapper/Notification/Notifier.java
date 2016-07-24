package uk.co.ben_gibson.repositorymapper.Notification;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import org.jetbrains.annotations.NotNull;

/**
 * Responsible for sending system notifications.
 */
public class Notifier
{

    /**
     * Error notification.
     */
    public static void errorNotification(@NotNull String message)
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