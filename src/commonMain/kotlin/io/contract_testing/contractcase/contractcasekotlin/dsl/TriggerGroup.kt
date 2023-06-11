package io.contract_testing.contractcase.contractcasekotlin.dsl

/**
 * TODO: Make a DSL builder for this
 */
data class TriggerGroup<T>(
    val name: String,
    val trigger: Trigger<T>,
    val testResponses: Map<String, TestResponseFunction<T>>,
    val testErrorResponses: Map<String, TestErrorResponseFunction>
)

typealias Trigger<T> = (Map<String, Any>) -> T
typealias TestResponseFunction<T> = (T) -> Unit
typealias TestErrorResponseFunction = (Exception) -> Unit
