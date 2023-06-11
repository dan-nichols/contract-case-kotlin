package io.contract_testing.contractcase.contractcasekotlin.dsl

import io.contract_testing.contractcase.case_example_mock_types.base.AnyMockDescriptor

// TODO: Make a DSL builder for this
// TODO: Create DSL's for the subclasses of AnyMockDescriptor
class ExampleDefinition<M : AnyMockDescriptor?>(
    val states: List<Any>,
    val definition: M
)
