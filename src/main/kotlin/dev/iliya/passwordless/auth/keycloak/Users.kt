package dev.iliya.passwordless.auth.keycloak

import org.keycloak.models.UserModel
import org.keycloak.models.credential.OTPCredentialModel
import org.keycloak.models.credential.dto.OTPCredentialData
import org.keycloak.models.credential.dto.OTPSecretData
import org.keycloak.models.utils.HmacOTP
import org.keycloak.models.utils.TimeBasedOTP
import kotlin.random.Random

private object Attributes {
    const val PASSWORDLESS_AUTHENTICATION_ENABLED = "passwordlessAuthenticationEnabled"
}

private object KeycloakOTP {
    const val NAME = "Passwordless Authentication OTP"
    const val TYPE = "Passwordless OTP"
    const val SECRET_LENGTH = 32
    const val DIGITS = 2
    const val ALGORITHM = HmacOTP.DEFAULT_ALGORITHM
}

class ResponseOption(
    val data: String,
    val isChallengeResponse: Boolean,
) {
    companion object {
        const val SIZE = 3
    }
}

private fun UserModel.getCustomAttribute(key: String, defaultValue: Any? = null): String? {
    return this.getFirstAttribute(key) ?: defaultValue?.toString()
}

private fun UserModel.setCustomAttribute(key: String, value: Any?) {
    value?.let { this.setSingleAttribute(key, it.toString()) }
}

private fun UserModel.createOTPCredentialModel() {
    val secretValue = HmacOTP.generateSecret(KeycloakOTP.SECRET_LENGTH)
    val digits = KeycloakOTP.DIGITS
    val initialCounter = Random.nextInt(from = 1, until = 1000)
    val algorithm = KeycloakOTP.ALGORITHM
    val otpCredentialModel = OTPCredentialModel.createHOTP(secretValue, digits, initialCounter, algorithm)
    otpCredentialModel.type = KeycloakOTP.TYPE
    otpCredentialModel.userLabel = KeycloakOTP.NAME
    this.credentialManager().createStoredCredential(otpCredentialModel)
}

private fun UserModel.getOTPCredentialModel(): OTPCredentialModel? {
    val name = KeycloakOTP.NAME
    val type = KeycloakOTP.TYPE
    val credentialModel = this.credentialManager().getStoredCredentialByNameAndType(name, type)
    return credentialModel?.let { OTPCredentialModel.createFromCredentialModel(it) }
}

private fun HmacOTP.generateChallengeNumber(secretData: OTPSecretData, credentialData: OTPCredentialData): String {
    return this.generateHOTP(secretData.value, credentialData.counter)
}

private fun OTPCredentialModel.generateResponseOptions(): List<ResponseOption> {
    val secretData = this.otpSecretData
    val credentialData = this.otpCredentialData
    val hmacOTP = HmacOTP(credentialData.digits, credentialData.algorithm, TimeBasedOTP.DEFAULT_DELAY_WINDOW)
    val responseOptions = List(ResponseOption.SIZE) { index ->
        val challengeNumber = hmacOTP.generateChallengeNumber(secretData, credentialData)
        this.updateCounter(credentialData.counter + 1)
        return@List ResponseOption(challengeNumber, index == ResponseOption.SIZE - 1)
    }
    check(responseOptions.last().isChallengeResponse)
    return responseOptions
}

fun UserModel.generateResponseOptions(): List<ResponseOption> {
    check(passwordlessNumberMatchingConfigured)
    val otpCredentialModel = this.getOTPCredentialModel()
    checkNotNull(otpCredentialModel)
    val responseOptions = otpCredentialModel.generateResponseOptions()
    this.credentialManager().updateStoredCredential(otpCredentialModel)
    return responseOptions
}

fun UserModel.configurePasswordlessAuthentication(enabled: Boolean) {
    this.passwordlessAuthenticationEnabled = enabled
    if (!this.passwordlessNumberMatchingConfigured) createOTPCredentialModel()
}

private val UserModel.passwordlessNumberMatchingConfigured: Boolean
    get() = getOTPCredentialModel() != null

var UserModel.passwordlessAuthenticationEnabled: Boolean
    get() = this.getCustomAttribute(Attributes.PASSWORDLESS_AUTHENTICATION_ENABLED, false).toBoolean()
    private set(value) = this.setCustomAttribute(Attributes.PASSWORDLESS_AUTHENTICATION_ENABLED, value)
