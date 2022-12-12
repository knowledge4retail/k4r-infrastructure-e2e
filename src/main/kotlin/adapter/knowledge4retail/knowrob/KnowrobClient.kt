package adapter.knowledge4retail.knowrob

import adapter.knowledge4retail.Knowledge4RetailClient
import adapter.knowledge4retail.Knowledge4RetailClientConfig
import adapter.knowledge4retail.OauthKnowledge4RetailClient
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.runBlocking

class KnowrobClient(
    private val endpoint: String,
    client: Knowledge4RetailClient
) {
    private val apiClient: HttpClient

    init {
        apiClient = client.create()
    }

    fun performQuery(request: String): HttpResponse {
        return runBlocking {
            apiClient.request(endpoint) {
                method = HttpMethod.Post
                setBody(ByteArrayContent(request.toByteArray(), ContentType.Application.Json))
            }
        }
    }
}