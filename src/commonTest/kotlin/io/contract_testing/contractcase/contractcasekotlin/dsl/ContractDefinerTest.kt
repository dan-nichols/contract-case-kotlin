package io.contract_testing.contractcase.contractcasekotlin.dsl

import io.contract_testing.contractcase.case_example_mock_types.http.HttpExample
import io.contract_testing.contractcase.case_example_mock_types.http.WillSendHttpRequest
import io.contract_testing.contractcase.contractcasekotlin.boundary.build
import io.contract_testing.contractcase.contractcasekotlin.example.client.ProductService
import io.contract_testing.contractcase.test_equivalence_matchers.http.HttpRequest
import io.contract_testing.contractcase.test_equivalence_matchers.http.HttpRequestExample
import io.contract_testing.contractcase.test_equivalence_matchers.http.HttpResponse
import io.contract_testing.contractcase.test_equivalence_matchers.http.HttpResponseExample
import io.contract_testing.contractcase.test_equivalence_matchers.strings.AnyString
import kotlinx.coroutines.runBlocking
import kotlin.test.assertEquals
import kotlin.test.Test

class ContractDefinerTest {

    private val contract = ContractDefiner(
        contractCaseConfig {
            providerName = "Kotlin Example HTTP Server"
            consumerName = "Kotlin Example HTTP Client"
            publish = PublishType.NEVER
        }
    )

    /**
     * TODO: This is a test/example usage and doesn't belong here
     */
    fun example() {
        val config = contractCaseConfig {
            providerName = "hello"

//        Lateinit var should always be defined - write a test for this and throw a useful error
//        consumerName = "mmmm"

            stateSetupHandler("Server is up") {
                mapOf("userId" to 42)
            }

            stateSetupHandler("Server is down") {
                val mockHealthStatus = false // do some stuff that doesn't return state
                null
            }

            stateHandler("A user exists") {
                setup {
                    mapOf("userId" to 42)
                }
                teardown { }
            }

//        stateHandlers = mapOf(
////            "Server is up" to {
////                mapOf("userId" to 42)
////            },
//

//        )
        }
    }

    @Test
    fun getProduct() {
        contract.runExample(
            ExampleDefinition(
                states = emptyList(),
                definition = WillSendHttpRequest(
                    build(HttpExample::builder) {
                        request(HttpRequest(
                            build(HttpRequestExample::builder) {
                                path("/products/10")
                                method("GET")
                            }
                        ))

                        response(HttpResponse(
                            build(HttpResponseExample::builder) {
                                status(200)
                                body(
                                    mapOf(
                                        "name" to AnyString("product name"),
                                        "type" to AnyString("product series"),
                                        "id" to AnyString("5cc989d0-d800-434c-b4bb-b1268499e850")
                                    )
                                )
                            }
                        ))
                    }
                )
            ),
            contractCaseConfigWithTrigger<String> {
                logLevel = LogLevel.DEEP_MAINTAINER_DEBUG
                trigger = { config ->
                    runBlocking {
                        // Do we need to do this? .replace("localhost", "127.0.0.1")
                        ProductService(
                            baseUrl = (config["baseUrl"] as String).replace("localhost", "127.0.0.1")
                        ).getProduct("10")
                    }
                }
                testResponse = { product ->
                    assertEquals("5cc989d0-d800-434c-b4bb-b1268499e850", product)
                }
            }
        )
    }
}