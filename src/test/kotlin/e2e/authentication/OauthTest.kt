package e2e.authentication

import adapter.knowledge4retail.Knowledge4RetailClientConfig
import adapter.knowledge4retail.OauthKnowledge4RetailClient
import app.knowledge4retail.Profile
import app.knowledge4retail.Profile.Companion.toProfile
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.MethodNotAllowed
import io.ktor.http.HttpStatusCode.Companion.OK
import kotlinx.coroutines.runBlocking

class OauthTest: FreeSpec({

    val profile = (System.getenv(Profile.PROFILE_ENV_VAR)?: "SANDBOX").toProfile()

    "request endpoints with Oauth Credentials" - {
        listOf(
            profile.dtApiOauthEndpoint,
            profile.knowrobOauthEndpoint
        ).forEach { url: String ->
            "Request to $url with invalid Credentials should respond with 401 - Unauthorized" {
                // Arrange
                val k4rClient = OauthKnowledge4RetailClient(
                    Knowledge4RetailClientConfig.getForProfile(profile)
                        .copy("INVALID", "INVALID"))
                    .create()
                // Act & Assert
                shouldThrow<ClientRequestException> {
                    runBlocking {
                        k4rClient.request(url) {
                            method = HttpMethod.Get
                        }
                    }
                }.message.shouldContain(
                    "Invalid user credentials"
                )
            }
            "Request to $url with valid Credentials should respond with 200 - OK" {
                // Arrange
                val k4rClient = OauthKnowledge4RetailClient(
                    Knowledge4RetailClientConfig.getForProfile(profile)
                ).create()
                // Act
                val response = runBlocking {
                    k4rClient.request(url) {
                        method = HttpMethod.Get
                    }
                }
                // Assert
                response.status shouldBeIn arrayOf(MethodNotAllowed, OK)
            }
        }
    }
})