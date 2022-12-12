package e2e.knowrob

import adapter.knowledge4retail.AnonymousKnowledge4RetailClient
import adapter.knowledge4retail.Knowledge4RetailClientConfig
import adapter.knowledge4retail.OauthKnowledge4RetailClient
import adapter.knowledge4retail.knowrob.KnowrobClient
import adapter.knowledge4retail.knowrob.KnowrobResponse
import app.knowledge4retail.Profile
import app.knowledge4retail.Profile.Companion.PROFILE_ENV_VAR
import app.knowledge4retail.Profile.Companion.toProfile
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Unauthorized

class KnowrobTest : FunSpec({
    lateinit var profile: Profile
    lateinit var anonK4rClient: AnonymousKnowledge4RetailClient
    lateinit var oauthK4rClient: OauthKnowledge4RetailClient
    lateinit var clientConfig: Knowledge4RetailClientConfig
    lateinit var apiClient: KnowrobClient

    fun init() {
        profile = (System.getenv(PROFILE_ENV_VAR)?: "SANDBOX").toProfile()
        clientConfig = Knowledge4RetailClientConfig.getForProfile(profile)
        anonK4rClient = AnonymousKnowledge4RetailClient()
        oauthK4rClient = OauthKnowledge4RetailClient(clientConfig)
    }

    beforeEach {
        init()
    }

    test("Oauth request authenticates and returns correct response body") {
        // Arrange
        apiClient = KnowrobClient(profile.knowrobOauthEndpoint, oauthK4rClient)
        // Act
        val response = apiClient.doRequest()
        // Assert
        val knowrobResponse: KnowrobResponse = response.body()
        knowrobResponse.query shouldBeEqualComparingTo minimalKnowRobQueryFixture
        knowrobResponse.response.first()[minimalKnowRobQueryKeyFixture]?.shouldBeEqualComparingTo(
            minimalKnowRobQueryValueFixture
        )
    }

    test("Oauth Authentication fails on invalid credentials") {
        // Arrange
        apiClient =
            KnowrobClient(
                profile.knowrobOauthEndpoint,
                OauthKnowledge4RetailClient(clientConfig.copy(userName="INVALID", userPassword="INVALID"))
            )
        // Act & Assert
        shouldThrow<ClientRequestException> {
            apiClient.doRequest()
        }.message.shouldContain(
            "Invalid user credentials"
        )
    }

    test("Oauth request fails with no Bearer token present") {
        // Arrange
        apiClient = KnowrobClient(profile.knowrobOauthEndpoint, anonK4rClient)
        // Act
        val response = apiClient.doRequest()
        // Assert
        response.status shouldBe Unauthorized
    }

    test("X.509 request fails with no client certificate present") {
        // Arrange
        apiClient = KnowrobClient(profile.knowrobX509Endpoint, anonK4rClient)
        // Act
        val response = apiClient.doRequest()
        // Assert
        response.status shouldBe BadRequest
    }

    test("X.509 request fails with Authorization Header Present") {
        // Arrange
        apiClient = KnowrobClient(profile.knowrobX509Endpoint, oauthK4rClient)
        // Act
        val response = apiClient.doRequest()
        // Assert
        response.status shouldBe BadRequest
    }
})

fun KnowrobClient.doRequest() = this.performQuery(minimalKnowrobRequestBody)