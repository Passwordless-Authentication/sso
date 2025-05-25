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
        private const val TEMPLATE_NAME = "configure-passwordless-auth.ftl"
        private const val PASSWORDLESS_AUTH_ENABLED_ATTRIBUTE = "passwordlessEnabled"
        private const val PASSWORDLESS_AUTH_STATUS_ATTRIBUTE = "passwordlessStatus"
        private const val PASSWORDLESS_AUTH_ENABLED_LABEL_NAME = "passwordlessEnabledLabel"
        private const val PASSWORDLESS_AUTH_ENABLED_LABEL_VALUE = "Enabled"
        private const val PASSWORDLESS_AUTH_DISABLED_LABEL_NAME = "passwordlessDisabledLabel"
        private const val PASSWORDLESS_AUTH_DISABLED_LABEL_VALUE = "Disabled"
        private const val PASSWORDLESS_AUTH_ENABLE_BUTTON_LABEL_NAME = "passwordlessEnableButtonLabel"
        private const val PASSWORDLESS_AUTH_ENABLE_BUTTON_LABEL_VALUE = "Enable"
        private const val PASSWORDLESS_AUTH_DISABLE_BUTTON_LABEL_NAME = "passwordlessDisableButtonLabel"
        private const val PASSWORDLESS_AUTH_DISABLE_BUTTON_LABEL_VALUE = "Disable"
    }

    override fun evaluateTriggers(context: RequiredActionContext?) {
        // no-op
        // This required action is set via user request
    }

    override fun requiredActionChallenge(context: RequiredActionContext) {
        val passwordlessEnabled = context.user.passwordlessAuthenticationEnabled
        val form = context.form()
        form.setAttribute(PASSWORDLESS_AUTH_ENABLED_ATTRIBUTE, passwordlessEnabled)
        form.setAttribute(PASSWORDLESS_AUTH_STATUS_ATTRIBUTE, PASSWORDLESS_AUTH_STATUS_ATTRIBUTE)
        form.setAttribute(PASSWORDLESS_AUTH_ENABLED_LABEL_NAME, PASSWORDLESS_AUTH_ENABLED_LABEL_VALUE)
        form.setAttribute(PASSWORDLESS_AUTH_DISABLED_LABEL_NAME, PASSWORDLESS_AUTH_DISABLED_LABEL_VALUE)
        form.setAttribute(PASSWORDLESS_AUTH_ENABLE_BUTTON_LABEL_NAME, PASSWORDLESS_AUTH_ENABLE_BUTTON_LABEL_VALUE)
        form.setAttribute(PASSWORDLESS_AUTH_DISABLE_BUTTON_LABEL_NAME, PASSWORDLESS_AUTH_DISABLE_BUTTON_LABEL_VALUE)
        val challengeResponse = form.createForm(TEMPLATE_NAME)
        context.challenge(challengeResponse)
    }

    override fun processAction(context: RequiredActionContext) {
        val requestParams = context.httpRequest.decodedFormParameters
        val passwordlessStatus = requestParams.getFirst(PASSWORDLESS_AUTH_STATUS_ATTRIBUTE)
        requireNotNull(passwordlessStatus) { "Passwordless Authentication status should be set" }

        val user = context.user
        if (passwordlessStatus == PASSWORDLESS_AUTH_ENABLE_BUTTON_LABEL_VALUE.lowercase()) {
            user.configurePasswordlessAuthentication(enabled = true)
        } else if (passwordlessStatus == PASSWORDLESS_AUTH_DISABLE_BUTTON_LABEL_VALUE.lowercase()) {
            user.configurePasswordlessAuthentication(enabled = false)
        }

        user.removeRequiredAction(PROVIDER_ID)
        context.success()
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
