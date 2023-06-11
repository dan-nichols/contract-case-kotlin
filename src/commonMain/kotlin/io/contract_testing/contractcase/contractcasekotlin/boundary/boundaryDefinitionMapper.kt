package io.contract_testing.contractcase.contractcasekotlin.boundary

import io.contract_testing.contractcase.contractcasekotlin.dsl.ExampleDefinition
import io.contract_testing.contractcase.case_boundary.BoundaryMockDefinition

internal fun ExampleDefinition<*>.toMockDefinition(): BoundaryMockDefinition =
    build(BoundaryMockDefinition::builder) {
        definition(definition)
        states(states)
    }
