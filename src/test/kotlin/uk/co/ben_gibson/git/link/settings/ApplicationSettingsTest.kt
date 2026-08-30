package uk.co.ben_gibson.git.link.settings

import com.intellij.openapi.util.JDOMUtil
import com.intellij.util.xmlb.XmlSerializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ApplicationSettingsTest {
    private val persistedForm = javaClass.getResource("/settings/persisted-form.xml")!!.readText()

    private val customPlatform = ApplicationSettings.CustomPlatformSettings(
        id = "3fc8e330-760f-482f-8758-a0c34137d21c",
        displayName = "My Platform",
        baseUrl = "git.example.com",
        fileAtBranchTemplate = "a",
        fileAtCommitTemplate = "b",
        commitTemplate = "c"
    )

    private val registeredDomains = mapOf("0d0a2f7e-1c9a-4e46-9b4e-4f6a12c4a111" to setOf("git.example.com"))

    @Test
    fun `can persist settings`() {
        val settings = ApplicationSettings()

        settings.customPlatforms = listOf(customPlatform)
        settings.registeredDomains = registeredDomains

        assertThat(JDOMUtil.write(XmlSerializer.serialize(settings)))
            .isEqualTo(JDOMUtil.write(JDOMUtil.load(persistedForm)))
    }

    @Test
    fun `can load settings`() {
        val settings = XmlSerializer.deserialize(JDOMUtil.load(persistedForm), ApplicationSettings::class.java)

        assertThat(settings.customPlatforms)
            .containsExactly(customPlatform)

        assertThat(settings.registeredDomains)
            .isEqualTo(registeredDomains)
    }
}
