package uk.co.ben_gibson.git.link.git

import git4idea.repo.GitRemote
import java.util.UUID

class Hosts(private val hosts: Set<Host>) {
    fun getById(id: String) = getById(UUID.fromString(id))
    fun getById(id: UUID) = hosts.first { it.id == id }

    fun forRemote(remote: GitRemote) = remote.httpUrl()
        ?.host
        ?.let { host -> hosts.firstOrNull { it.baseUrl?.host == host } }

    fun toSet() = hosts.toHashSet()
    fun toArray() = hosts.toTypedArray()
}