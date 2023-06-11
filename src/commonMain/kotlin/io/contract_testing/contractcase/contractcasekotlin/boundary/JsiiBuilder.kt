package io.contract_testing.contractcase.contractcasekotlin.boundary

import io.contract_testing.contractcase.contractcasekotlin.dsl.ContractCaseDsl
import software.amazon.jsii.Builder

@ContractCaseDsl
internal fun <B : Builder<JsiiType>, JsiiType> build(
    constructor: () -> B,
    init: B.() -> Unit
): JsiiType {
    val builder = constructor()
    builder.init()
    return builder.build()
}
