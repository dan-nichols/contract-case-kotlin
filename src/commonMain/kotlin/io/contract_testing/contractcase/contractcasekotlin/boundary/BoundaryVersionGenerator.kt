package io.contract_testing.contractcase.contractcasekotlin.boundary

internal class BoundaryVersionGenerator {
    val versions: List<String>
        get() {
            val version = javaClass.getPackage().implementationVersion
            return listOf("Kotlin-DSL@${version ?: "UNKNOWN"}")
        }
}
