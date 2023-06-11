package io.contract_testing.contractcase.contractcasekotlin.example.client

import io.ktor.client.call.*
import io.ktor.client.request.*

interface HealthRepository {
    suspend fun getHealth(): ServerHealth
}

enum class ServerHealth {
    UP, DOWN;
}

class HealthService(baseUrl: String) : HealthRepository {

    private val client = buildKtorClient(baseUrl)

    override suspend fun getHealth(): ServerHealth =
        client.get("health").body()

    data class HealthResponse(
        val status: ServerHealth
    )
}
