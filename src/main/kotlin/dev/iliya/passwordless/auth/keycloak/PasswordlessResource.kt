package dev.iliya.passwordless.auth.keycloak

import io.quarkus.security.Authenticated
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.ws.rs.GET
import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.Response
import org.keycloak.models.KeycloakSession
import org.keycloak.services.managers.AppAuthManager

class PasswordlessResource(session: KeycloakSession) {
    private val auth = AppAuthManager.BearerTokenAuthenticator(session).authenticate()

    @Authenticated
    @Path("/auth/verify")
    @GET
    fun verify(
        @NotNull @NotBlank @NotEmpty @QueryParam("response") response: Int
    ): Response {
        val user = auth?.user ?: throw NotAuthorizedException("Bearer")
        val verified = user.verifyChallengeResponse(response)
        val responseData = ResponseData(
            username = user.username,
            verified = verified,
        )
        return Response.ok(responseData).build()
    }

    data class ResponseData(
        val username: String,
        val verified: Boolean,
    )
}
