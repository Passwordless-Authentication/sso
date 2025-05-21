package dev.iliya.passwordless.auth.keycloak

import org.keycloak.Config
import org.keycloak.authentication.RequiredActionContext
import org.keycloak.authentication.RequiredActionFactory
import org.keycloak.authentication.RequiredActionProvider
import org.keycloak.models.KeycloakSession
import org.keycloak.models.KeycloakSessionFactory

class ConfigurePasswordlessAuthRequiredAction : RequiredActionProvider, RequiredActionFactory {
    companion object {
        const val PROVIDER_ID = "configure-passwordless-auth-ra"
    }

    override fun evaluateTriggers(context: RequiredActionContext?) {
        // no-op
        // This required action is set via user request
    }

    override fun requiredActionChallenge(context: RequiredActionContext?) {
        // TODO
    }

    override fun processAction(context: RequiredActionContext?) {
        // TODO
    }

    override fun getId(): String? = PROVIDER_ID

    override fun getDisplayText(): String = "Configure Passwordless Auth Required Action"

    override fun create(session: KeycloakSession?): RequiredActionProvider? = this

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
