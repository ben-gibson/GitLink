package uk.co.ben_gibson.git.link.ui.notification

import uk.co.ben_gibson.git.link.git.Host

data class Notification(val title: String? = null, val message: String) {

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
            message = """
                GitLink taking too long? Would you like to disable checking for the commit on the remote? Disable
            """.trimIndent()
        )

        fun couldNotDetectGitHost(remoteHost: Host) = Notification(
            message = "GitLink could not detect your remote host and has defaulted to '%s'. You change this at any time from the settings."
                .trimIndent()
                .format(remoteHost.displayName)
        )

        fun remoteHostAutoDetected(remoteHost: Host) = Notification(
            message =  "GitLink has detected your remote host as '%s. You can change this anytime from the settings."
                .trimIndent()
                .format(remoteHost.displayName)
        )
    }
}
