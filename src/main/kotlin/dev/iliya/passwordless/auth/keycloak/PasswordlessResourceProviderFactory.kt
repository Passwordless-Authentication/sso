package dev.iliya.passwordless.auth.keycloak

import org.keycloak.Config
import org.keycloak.models.KeycloakSession
import org.keycloak.models.KeycloakSessionFactory
import org.keycloak.services.resource.RealmResourceProvider
import org.keycloak.services.resource.RealmResourceProviderFactory

class PasswordlessResourceProviderFactory : RealmResourceProviderFactory {
    companion object {
        const val PROVIDER_ID = "passwordless"
    }

    override fun create(session: KeycloakSession): RealmResourceProvider? {
        return PasswordlessResourceProvider(session)
    }

    override fun getId(): String? = PROVIDER_ID

    override fun init(config: Config.Scope?) {
        // no-op
    }

    override fun postInit(factory: KeycloakSessionFactory?) {
        // no-op
    }

    override fun close() {
        // no-op
    }
}
