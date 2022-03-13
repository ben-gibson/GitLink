package uk.co.ben_gibson.git.link.git

import git4idea.repo.GitRemote

class Hosts(private val hosts: Set<Host>) {
    fun getById(id: String) = getById(HostId(id))
    fun getById(id: HostId) = hosts.first { it.id == id }

    fun forRemote(remote: GitRemote) = remote.httpUrl()
        ?.host
        ?.let { host -> hosts.firstOrNull { it.baseUrl?.host == host } }

    fun toArray() = hosts.toTypedArray()
}