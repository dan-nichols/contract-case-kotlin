package io.contract_testing.contractcase.contractcasekotlin.example.client

import io.ktor.client.call.*
import io.ktor.client.request.*

interface ProductRepository {
    suspend fun getAllProducts(): List<String>
    suspend fun getProduct(id: String): String
}

class ProductService(baseUrl: String) : ProductRepository {

    private val client = buildKtorClient(baseUrl)

    override suspend fun getAllProducts(): List<String> =
        client.get("products").body()

    override suspend fun getProduct(id: String): String =
        client.get("products/$id").body()
}
