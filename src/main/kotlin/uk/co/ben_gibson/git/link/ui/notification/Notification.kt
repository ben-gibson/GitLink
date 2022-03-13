package uk.co.ben_gibson.git.link.ui.notification

import uk.co.ben_gibson.git.link.git.Host

data class Notification(val title: String, val message: String) {

    companion object {
        private const val DEFAULT_TITLE = "GitLink"

        fun repositoryNotFound() = Notification(DEFAULT_TITLE, "Could not find repository for selected file")
        fun remoteNotFound() = Notification(DEFAULT_TITLE, "Could not find remote")

        fun welcomeGuide() = Notification(
            DEFAULT_TITLE,
            """
                Thanks for installing GitLink. To get started, head over to the settings and configure your remote
                host.
            """.trimIndent()
        )

        fun performanceTips() = Notification(
            DEFAULT_TITLE,
            """
                GitLink taking too long? Try adjusting the settings to avoid calls to the remote repository
                to check the current commit status. 
            """.trimIndent()
        )

        fun couldNotDetectGitHost(remoteHost: Host) = Notification(
            DEFAULT_TITLE,
            """
                Could not detect your remote host automatically, '%s' has been set as the default remote host.
                You change this at any time from the plugin settings.
            """.trimIndent().format(remoteHost.displayName)
        )

        fun remoteHostAutoDetected(remoteHost: Host) = Notification(
            DEFAULT_TITLE,
            """
                '%s' has been detected as your remote host and automatically set it in the settings. You change this at any
                time from the plugin settings.
            """.trimIndent().format(remoteHost.displayName)
        )
    }
}
