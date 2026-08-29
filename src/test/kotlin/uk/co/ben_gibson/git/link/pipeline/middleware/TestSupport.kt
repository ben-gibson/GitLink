package uk.co.ben_gibson.git.link.pipeline.middleware

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import git4idea.repo.GitRemote
import git4idea.repo.GitRepository
import io.mockk.mockk
import uk.co.ben_gibson.git.link.Context
import uk.co.ben_gibson.git.link.pipeline.Pass
import uk.co.ben_gibson.git.link.platform.GitHub
import java.time.Instant
import java.time.InstantSource

fun pass(
    project: Project,
    repository: GitRepository = mockk(relaxed = true),
    remote: GitRemote = GitRemote("origin", listOf("git@github.com:user/repo.git"), emptyList(), emptyList(), emptyList()),
): Pass {
    val file = mockk<VirtualFile>()
    return Pass(
        project = project,
        context = Context.File(file),
        platform = GitHub(),
        repository = repository,
        remote = remote,
    )
}

class FixedInstantSource(private vararg val readings: Long) : InstantSource {
    private var index = 0

    override fun instant(): Instant = Instant.ofEpochMilli(readings[index++])
}
