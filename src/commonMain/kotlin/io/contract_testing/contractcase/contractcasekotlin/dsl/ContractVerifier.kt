package io.contract_testing.contractcase.contractcasekotlin.dsl

import io.contract_testing.contractcase.contractcasekotlin.boundary.BoundaryVersionGenerator
import io.contract_testing.contractcase.contractcasekotlin.boundary.toBoundaryConfig
import io.contract_testing.contractcase.contractcasekotlin.boundary.toBoundaryFailure
import io.contract_testing.contractcase.contractcasekotlin.boundary.verify
import io.contract_testing.contractcase.case_boundary.BoundaryContractVerifier
import io.contract_testing.contractcase.case_boundary.BoundarySuccess
import io.contract_testing.contractcase.case_boundary.IRunTestCallback
import io.contract_testing.contractcase.contractcasekotlin.LogPrinter

/**
 * TODO: Create a nicer dsl for this
 * TODO: Write Kdoc for this class/methods
 */
class ContractVerifier(private val config: ContractCaseConfig<*>) {
    private val verifier: BoundaryContractVerifier

    init {
        val logPrinter = LogPrinter()
        verifier = BoundaryContractVerifier(
            config.toBoundaryConfig("VERIFICATION"),
            IRunTestCallback { testName, invoker -> // TODO replace this with something that knows about JUnit/Kotlin Test
                try {
                    // TODO: Figure out why there was a nested try/catch here...
                    val result = invoker.verify()
                    result.verify()

                    return@IRunTestCallback BoundarySuccess()
                } catch (e: Exception) {
                    return@IRunTestCallback e.toBoundaryFailure()
                }
            },
            logPrinter,
            logPrinter,
            BoundaryVersionGenerator().versions
        )
    }

    fun runVerification(configOverrides: ContractCaseConfig<*>?) {
        val mergedConfig = configOverrides?.mergeConfig(config) ?: config
        verifier.runVerification(mergedConfig.toBoundaryConfig("VERIFICATION"))
    }
}
