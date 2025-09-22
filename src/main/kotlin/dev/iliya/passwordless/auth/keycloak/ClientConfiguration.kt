package dev.iliya.passwordless.auth.keycloak

import java.net.URI
import java.util.*

object ClientConfiguration {
    private var httpBaseUrl: String
    private var wsBaseUrl: String
    private var responseOptionsEndpoint: String
    private var wsEndpoint: String

    val responseOptionsUrl: URI
        get() = URI.create("$httpBaseUrl$responseOptionsEndpoint")

    val wsUrl: URI
        get() = URI.create("$wsBaseUrl$wsEndpoint")

    init {
        this.javaClass.classLoader.getResourceAsStream("client.properties").use { inputStream ->
            val properties = Properties()
            properties.load(inputStream)
            httpBaseUrl = getSystemProperty(properties.getProperty("httpBaseUrl"))
            wsBaseUrl = getSystemProperty(properties.getProperty("wsBaseUrl"))
            responseOptionsEndpoint = getSystemProperty(properties.getProperty("responseOptionsEndpoint"))
            wsEndpoint = getSystemProperty(properties.getProperty("wsEndpoint"))
        }
    }

    private fun getSystemProperty(input: String): String {
        // matches ${value:default_value}
        val pattern = "\\$\\{([^:}]+)(?::([^}]*))?}".toRegex()
        val matcher = pattern.findAll(input)
        // if not matched with regex return the actual value
        var output = input

        for (match in matcher) {
            val property = match.groupValues[1]
            val defaultValue = match.groupValues[2]
            val value = System.getenv(property) ?: System.getProperty(property, defaultValue)
            output = output.replace(match.value, value)
        }

        return output
    }
}
