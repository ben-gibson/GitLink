package uk.co.ben_gibson.git.link.pipeline.middleware

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import git4idea.repo.GitRemote
import git4idea.repo.GitRepository
import io.mockk.mockk
import uk.co.ben_gibson.git.link.ContextCurrentFile
import uk.co.ben_gibson.git.link.pipeline.Pass
import uk.co.ben_gibson.git.link.platform.GitHub

fun pass(
    project: Project,
    repository: GitRepository = mockk(relaxed = true),
    remote: GitRemote = GitRemote("origin", listOf("git@github.com:user/repo.git"), emptyList(), emptyList(), emptyList()),
): Pass {
    val file = mockk<VirtualFile>()
    return Pass(
        project = project,
        context = ContextCurrentFile(file),
        platform = GitHub(),
        repository = repository,
        remote = remote,
    )
}
