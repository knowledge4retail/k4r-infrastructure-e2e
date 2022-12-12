package adapter.knowledge4retail

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class OauthKnowledge4RetailClient(
    private val config: Knowledge4RetailClientConfig
): Knowledge4RetailClient {
    override fun create(): HttpClient {
        val authenticationEndpoint = config.tokenEndpoint
        val tokenClient = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
            install(Logging)
        }

        return HttpClient(CIO) {
            expectSuccess = false

            install(ContentNegotiation) {
                json()
            }

            install(Auth) {
                lateinit var token: KeycloakToken

                bearer {
                    loadTokens {
                        println("initializing OauthKnowledge4RetailClient with username ${config.userName}")
                        val response =
                            tokenClient.submitForm(authenticationEndpoint, formParameters = Parameters.build {
                                append("client_id", config.clientId)
                                append("grant_type", "password")
                                append("username", config.userName)
                                append("password", config.userPassword)
                            })
                        if (response.status == HttpStatusCode.OK) {
                            token = response.body()
                            BearerTokens(
                                accessToken = token.accessToken, refreshToken = token.refreshToken
                            )
                        } else {
                            throw ClientRequestException(response, response.bodyAsText())
                        }
                    }
                }
            }
        }
    }
}


@Serializable
data class KeycloakToken(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val accessExpiresIn: Int,
    @SerialName("refresh_expires_in") val refreshExpiresIn: Int,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("not-before-policy") val notBeforePolicy: Int,
    @SerialName("session_state") val sessionState: String,
    @SerialName("scope") val scope: String,
)