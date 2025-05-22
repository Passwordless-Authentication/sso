package dev.iliya.passwordless.auth.keycloak

import org.keycloak.Config
import org.keycloak.authentication.Authenticator
import org.keycloak.authentication.AuthenticatorFactory
import org.keycloak.authentication.ConfigurableAuthenticatorFactory
import org.keycloak.models.AuthenticationExecutionModel
import org.keycloak.models.KeycloakSession
import org.keycloak.models.KeycloakSessionFactory
import org.keycloak.provider.ProviderConfigProperty

class PasswordlessFormAuthenticatorFactory : AuthenticatorFactory {
    companion object {
        const val PROVIDER_ID = "passwordless-authenticator"
        val SINGLETON = PasswordlessFormAuthenticator()
    }

    override fun create(session: KeycloakSession?): Authenticator = SINGLETON

    override fun getId(): String = PROVIDER_ID

    override fun getDisplayType(): String = "Passwordless Authenticator Form"

    override fun getHelpText(): String = "Passwordless Authenticator Form"

    override fun getReferenceCategory(): String? = null

    override fun isConfigurable(): Boolean = false

    override fun isUserSetupAllowed(): Boolean = true

    override fun getConfigProperties(): MutableList<ProviderConfigProperty>? = null

    override fun getRequirementChoices(): Array<AuthenticationExecutionModel.Requirement> {
        return ConfigurableAuthenticatorFactory.REQUIREMENT_CHOICES
    }

    override fun init(scope: Config.Scope?) {
        // no-op
    }

    override fun postInit(session: KeycloakSessionFactory?) {
        // no-op
    }

    override fun close() {
        // no-op
    }
}
