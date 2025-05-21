package dev.iliya.passwordless.auth.keycloak

import org.keycloak.models.UserModel

private object Attributes {
    const val PASSWORDLESS_AUTHENTICATION_ENABLED = "passwordlessAuthenticationEnabled"
}

private fun UserModel.getCustomAttribute(key: String, defaultValue: Any? = null): String? {
    return this.getFirstAttribute(key) ?: defaultValue?.toString()
}

private fun UserModel.setCustomAttribute(key: String, value: Any?) {
    value?.let { this.setSingleAttribute(key, it.toString()) }
}

var UserModel.passwordlessAuthenticationEnabled: Boolean
    get() = this.getCustomAttribute(Attributes.PASSWORDLESS_AUTHENTICATION_ENABLED, false).toBoolean()
    set(value) = this.setCustomAttribute(Attributes.PASSWORDLESS_AUTHENTICATION_ENABLED, value)
