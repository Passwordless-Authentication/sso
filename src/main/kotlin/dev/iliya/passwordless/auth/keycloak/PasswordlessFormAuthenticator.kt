package dev.iliya.passwordless.auth.keycloak

import jakarta.ws.rs.core.Response
import org.keycloak.authentication.AuthenticationFlowContext
import org.keycloak.authentication.Authenticator
import org.keycloak.authentication.authenticators.browser.AbstractUsernameFormAuthenticator
import org.keycloak.forms.login.LoginFormsProvider
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.models.UserModel

class PasswordlessFormAuthenticator : AbstractUsernameFormAuthenticator(), Authenticator {
    override fun authenticate(context: AuthenticationFlowContext) {
        context.success()
    }

    override fun action(context: AuthenticationFlowContext) {
        // actually send the notification
    }

    override fun configuredFor(session: KeycloakSession?, realm: RealmModel?, user: UserModel?): Boolean {
        return user?.passwordlessAuthenticationEnabled ?: false
    }

    override fun createLoginForm(form: LoginFormsProvider): Response {
        // create a passwordless auth form
        // send notification button
        TODO()
    }

    override fun requiresUser(): Boolean = true

    override fun close() {
        // no-op
    }

    override fun setRequiredActions(session: KeycloakSession?, realm: RealmModel?, user: UserModel?) {
        // no-op
    }
}
