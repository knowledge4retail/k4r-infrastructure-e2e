package adapter.knowledge4retail

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*

class AnonymousKnowledge4RetailClient: Knowledge4RetailClient {
    override fun create(): HttpClient {
        return HttpClient(CIO) {
            expectSuccess = false
            install(ContentNegotiation) {
                json()
            }
            install(Logging)
        }
    }
}