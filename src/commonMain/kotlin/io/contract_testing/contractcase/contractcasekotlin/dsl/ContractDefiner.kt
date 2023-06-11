package io.contract_testing.contractcase.contractcasekotlin.dsl

import io.contract_testing.contractcase.contractcasekotlin.boundary.BoundaryVersionGenerator
import io.contract_testing.contractcase.case_boundary.BoundaryContractDefiner
import io.contract_testing.contractcase.case_example_mock_types.base.AnyMockDescriptor
import io.contract_testing.contractcase.contractcasekotlin.LogPrinter
import io.contract_testing.contractcase.contractcasekotlin.boundary.*
import io.contract_testing.contractcase.contractcasekotlin.boundary.toBoundaryConfig
import io.contract_testing.contractcase.contractcasekotlin.boundary.toFailingExample
import io.contract_testing.contractcase.contractcasekotlin.boundary.toMockDefinition
import io.contract_testing.contractcase.contractcasekotlin.boundary.toSuccessExample

/**
 * TODO: Create a nicer dsl for this
 * TODO: Write Kdoc for this class/methods
 */
class ContractDefiner(private val config: ContractCaseConfig<*>) {
    private val definer: BoundaryContractDefiner

    init {
        val logPrinter = LogPrinter()
        definer = BoundaryContractDefiner(
            config.toBoundaryConfig(TEST_RUN_ID),
            logPrinter,
            logPrinter,
            BoundaryVersionGenerator().versions
        )
    }

    fun <T, M : AnyMockDescriptor?> runExample(
        definition: ExampleDefinition<M>,
        additionalConfig: ContractCaseConfig<T>?
    ) {
        val mergedConfig = additionalConfig?.mergeConfig(config) ?: config

        definer.runExample(
            definition.toMockDefinition(),
            mergedConfig.toSuccessExample(TEST_RUN_ID)
        ).verify()
    }

    fun <T, M : AnyMockDescriptor?> runThrowingExample(
        definition: ExampleDefinition<M>,
        additionalConfig: ContractCaseConfig<T>?
    ) {
        val mergedConfig = additionalConfig?.mergeConfig(config) ?: config

        definer.runRejectingExample(
            definition.toMockDefinition(),
            mergedConfig.toFailingExample(TEST_RUN_ID)
        ).verify()
    }

    companion object {
        private const val TEST_RUN_ID = "KOTLIN"
    }
}
