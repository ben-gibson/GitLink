package uk.co.ben_gibson.git.link.ui.actions.vcslog

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.vcs.log.Hash
import com.intellij.vcs.log.VcsLog
import com.intellij.vcs.log.VcsLogDataKeys
import com.intellij.vcs.log.VcsUser
import com.intellij.vcs.log.impl.HashImpl
import com.intellij.vcs.log.impl.VcsFileStatusInfo
import git4idea.GitCommit
import git4idea.history.GitCommitRequirements
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import uk.co.ben_gibson.git.link.ContextCommit
import uk.co.ben_gibson.git.link.ui.actions.Action

class BrowserActionTest {
    @ParameterizedTest(name = "{0} retrieves full commit hash from VCS log")
    @ValueSource(classes = [BrowserAction::class, CopyAction::class, MarkdownAction::class])
    fun `retrieves full commit hash from VCS log`(actionClassToTest: Class<Action>) {
        val projectDummy = mockk<Project>()
        val anActionEventMock = prepareActionEventWithSingleSelectedCommitInVcsLog(
            commitHash = "b032a0707beac9a2f24b1b7d97ee4f7156de182c"
        )
        val sut = actionClassToTest.constructors[0].newInstance() as Action

        val context = sut.buildContext(projectDummy, anActionEventMock)

        assertNotNull(context)
        assertInstanceOf(ContextCommit::class.java, context)
        assertEquals("b032a0707beac9a2f24b1b7d97ee4f7156de182c", (context as ContextCommit).commit.toString())
    }

    private fun prepareActionEventWithSingleSelectedCommitInVcsLog(commitHash: String): AnActionEvent {
        val authorDummy = mockk<VcsUser>()
        val commit = GitCommit(
            mockk<Project>(),
            HashImpl.build(commitHash),
            emptyList<Hash>(),
            0,
            mockk<VirtualFile>(),
            "subject",
            authorDummy,
            "commit message",
            authorDummy,
            0,
            emptyList<List<VcsFileStatusInfo>>(),
            mockk<GitCommitRequirements>()
        )

        val vcsLog = mockk<VcsLog>()
        every { vcsLog.selectedDetails } returns listOf(commit)

        val anActionEvent = mockk<AnActionEvent>()
        every { anActionEvent.getData(VcsLogDataKeys.VCS_LOG) } returns vcsLog

        return anActionEvent
    }
}
