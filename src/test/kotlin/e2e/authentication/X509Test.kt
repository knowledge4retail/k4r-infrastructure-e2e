package e2e.authentication

import adapter.knowledge4retail.AnonymousKnowledge4RetailClient
import app.knowledge4retail.Profile
import app.knowledge4retail.Profile.Companion.toProfile
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import kotlinx.coroutines.runBlocking

class X509Test: FreeSpec({

    val profile = (System.getenv(Profile.PROFILE_ENV_VAR)?: "SANDBOX").toProfile()
    val k4rClient = AnonymousKnowledge4RetailClient().create()

    "request endpoints without client certificate" - {
        listOf(
            profile.dtApiX509Endpoint,
            profile.knowrobX509Endpoint,
            profile.distanceMatrixX509Endpoint
        ).forEach { url: String ->
            "Request to $url should fail with 400 - Bad Request" {
                val response = runBlocking {
                    k4rClient.request(url) {
                        method = HttpMethod.Get
                    }
                }
                response.status shouldBe BadRequest
            }
        }
    }
})