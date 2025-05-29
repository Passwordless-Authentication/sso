package dev.iliya.passwordless.auth.keycloak

import org.keycloak.models.KeycloakSession
import org.keycloak.services.resource.RealmResourceProvider

class PasswordlessResourceProvider(
    private val session: KeycloakSession,
): RealmResourceProvider {
    override fun getResource(): Any? {
        return PasswordlessResource(session)
    }

    override fun close() {
        // no-op
    }
}
