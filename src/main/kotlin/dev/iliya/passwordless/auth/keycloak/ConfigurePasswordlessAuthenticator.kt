package dev.iliya.passwordless.auth.keycloak

import org.keycloak.Config
import org.keycloak.authentication.AuthenticationFlowContext
import org.keycloak.authentication.Authenticator
import org.keycloak.authentication.AuthenticatorFactory
import org.keycloak.models.AuthenticationExecutionModel
import org.keycloak.models.KeycloakSession
import org.keycloak.models.KeycloakSessionFactory
import org.keycloak.models.RealmModel
import org.keycloak.models.UserModel
import org.keycloak.provider.ProviderConfigProperty

class ConfigurePasswordlessAuthenticator : Authenticator, AuthenticatorFactory {
    companion object {
        const val PROVIDER_ID = "configure-passwordless-authenticator"
        val REQUIREMENT_CHOICES = arrayOf(AuthenticationExecutionModel.Requirement.REQUIRED)
    }

    override fun authenticate(context: AuthenticationFlowContext) {
        context.user.addRequiredAction(ConfigurePasswordlessAuthRequiredAction.PROVIDER_ID)
        context.success()
    }

    override fun getId(): String? = PROVIDER_ID

    override fun getDisplayType(): String? = "Configure passwordless authentication"

    override fun getHelpText(): String? = "Sets the required action to configure passwordless authentication"

    override fun requiresUser(): Boolean = true

    override fun configuredFor(session: KeycloakSession?, realm: RealmModel?, user: UserModel?): Boolean = true

    override fun create(session: KeycloakSession?): Authenticator? = this

    override fun getReferenceCategory(): String? = null

    override fun isConfigurable(): Boolean = false

    override fun isUserSetupAllowed(): Boolean = false

    override fun getConfigProperties(): List<ProviderConfigProperty?>? = null

    override fun getRequirementChoices(): Array<out AuthenticationExecutionModel.Requirement?>? = REQUIREMENT_CHOICES

    override fun action(context: AuthenticationFlowContext?) {
        // no-op
    }

    override fun setRequiredActions(session: KeycloakSession?, realm: RealmModel?, user: UserModel?) {
        // no-op
    }

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
