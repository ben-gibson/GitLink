package uk.co.ben_gibson.git.link.ui.notification

data class Notification(val title : String, val message : String) {
    companion object {
        fun welcomeGuide() : Notification {
            return Notification(
                "GitLink",
                """
                    Thanks for installing GitLink. To get started, head over to the settings and configure your remote
                    host.
                """.trimIndent()
            )
        }
    }
}
