package uk.co.ben_gibson.git.link.git

import java.net.URI
import java.util.UUID

class Hosts(private val hosts: Set<Host>) {

    fun getById(id: String) = getById(UUID.fromString(id))
    fun getById(id: UUID) = hosts.firstOrNull() { it.id == id }

    fun getByHostDomain(domain: URI) = hosts.firstOrNull { host -> host.domains.contains(domain) }

    fun toSet() = hosts.toSet()
    fun toList() = hosts.toList()
    fun toArray() = hosts.toTypedArray()
}