package uk.co.ben_gibson.git.link.git

import git4idea.repo.GitRemote
import java.util.UUID

class Hosts(private val hosts: Set<Host>) {

    fun getById(id: String) = getById(UUID.fromString(id))
    fun getById(id: UUID) = hosts.firstOrNull() { it.id == id }

    fun forRemote(remote: GitRemote) = remote.httpUrl()
        ?.host
        ?.let { remoteHost -> hosts.firstOrNull { host -> host.baseUrls.map { it.host }.contains(remoteHost) } }

    fun toSet() = hosts.toHashSet()
    fun toList() = hosts.toList()
    fun toArray() = hosts.toTypedArray()
}