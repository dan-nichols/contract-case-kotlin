package io.contract_testing.contractcase.contractcasekotlin.example.client

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

fun buildKtorClient(baseUrl: String) = HttpClient {
    install(Logging)
    install(ContentNegotiation) {
        json()
    }

    defaultRequest {
        url(baseUrl)
        header("Accept", "application/json")
    }
}
