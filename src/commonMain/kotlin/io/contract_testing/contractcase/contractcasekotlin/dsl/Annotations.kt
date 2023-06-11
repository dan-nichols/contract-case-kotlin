package io.contract_testing.contractcase.contractcasekotlin.dsl

/**
 * A marker annotations for Contract Case Builder DSLs.
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
internal annotation class ContractCaseDsl
