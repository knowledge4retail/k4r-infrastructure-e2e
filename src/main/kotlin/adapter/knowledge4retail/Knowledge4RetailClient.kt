package adapter.knowledge4retail

import io.ktor.client.*

interface Knowledge4RetailClient {
    fun create(): HttpClient
}