package dev.iliya.passwordless.auth.keycloak

import jakarta.ws.rs.core.Response
import org.keycloak.authentication.AuthenticationFlowContext
import org.keycloak.authentication.AuthenticationFlowError
import org.keycloak.authentication.Authenticator
import org.keycloak.authentication.authenticators.browser.AbstractUsernameFormAuthenticator
import org.keycloak.forms.login.LoginFormsProvider
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.models.UserModel
import org.keycloak.sessions.AuthenticationSessionModel

class PasswordlessFormAuthenticator : AbstractUsernameFormAuthenticator(), Authenticator {
    private companion object {
        const val SEND_NOTIFICATION_TEMPLATE = "passwordless-login-send-notification.ftl"
        const val VERIFY_CHALLENGE_TEMPLATE = "passwordless-login-verify-challenge.ftl"
        const val CHALLENGE_RESPONSE_ATTRIBUTE = "challengeResponse"
        const val NOTIFICATION_SENT_NOTE_KEY = "notificationSent"
    }

    override fun authenticate(context: AuthenticationFlowContext) {
        if (context.authenticationSession.authNotificationSent) {
            val challengeVerified = context.user.passwordlessAuthenticationChallengeVerified
            if (challengeVerified == true) {
                context.success()
                return
            } else if (challengeVerified == false) {
                val response = challenge(context, "Challenge Failed. Invalid challenge response")
                context.authenticationSession.removeAuthNote(NOTIFICATION_SENT_NOTE_KEY)
                context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, response)
                context.resetFlow()
                return
            }

            context.challenge(createVerifyChallengeForm(context))
            return
        }

        val challengeResponse = challenge(context, null)
        context.challenge(challengeResponse)
    }

    override fun action(context: AuthenticationFlowContext) {
        if (context.authenticationSession.authNotificationSent) {
            val resend = context.httpRequest.decodedFormParameters.getFirst("resend").toBoolean()
            if (resend) {
                context.challenge(sendVerifyNotification(context))
                return
            }

            context.challenge(createVerifyChallengeForm(context))
            return
        }

        context.challenge(sendVerifyNotification(context))
    }

    override fun configuredFor(session: KeycloakSession?, realm: RealmModel?, user: UserModel?): Boolean {
        return user?.passwordlessAuthenticationEnabled ?: false
    }

    override fun createLoginForm(form: LoginFormsProvider): Response {
        return form.createForm(SEND_NOTIFICATION_TEMPLATE)
    }

    private fun createVerifyChallengeForm(context: AuthenticationFlowContext): Response {
        val form = context.form()
        form.setChallengeResponseFormAttribute(context.authenticationSession.challengeResponse)
        return form.createForm(VERIFY_CHALLENGE_TEMPLATE)
    }

    private fun sendVerifyNotification(context: AuthenticationFlowContext): Response {
        val responseOptions = context.user.generateNumberMatchingResponseOptions()
        val challengeResponse = responseOptions.first { it.isChallengeResponse }
        // TODO: send the notification
        context.authenticationSession.authNotificationSent = true
        context.authenticationSession.challengeResponse = challengeResponse.data
        val form = context.form()
        form.setChallengeResponseFormAttribute(challengeResponse.data)
        return createVerifyChallengeForm(context)
    }

    private var AuthenticationSessionModel.authNotificationSent: Boolean
        get() = this.getAuthNote(NOTIFICATION_SENT_NOTE_KEY)?.toBoolean() == true
        set(value) {
            this.setAuthNote(NOTIFICATION_SENT_NOTE_KEY, value.toString())
        }

    private var AuthenticationSessionModel.challengeResponse: Int
        get() = this.getAuthNote(CHALLENGE_RESPONSE_ATTRIBUTE).toInt()
        set(value) {
            this.setAuthNote(CHALLENGE_RESPONSE_ATTRIBUTE, value.toString())
        }

    private fun LoginFormsProvider.setChallengeResponseFormAttribute(challengeResponse: Int) {
        this.setAttribute(CHALLENGE_RESPONSE_ATTRIBUTE, challengeResponse)
    }

    override fun requiresUser(): Boolean = true

    override fun close() {
        // no-op
    }

    override fun setRequiredActions(session: KeycloakSession?, realm: RealmModel?, user: UserModel?) {
        // no-op
    }
}
