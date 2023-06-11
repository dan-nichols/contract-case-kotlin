package io.contract_testing.contractcase.contractcasekotlin.boundary

import io.contract_testing.contractcase.case_boundary.ContractCaseBoundaryConfig
import io.contract_testing.contractcase.case_boundary.UserNamePassword
import io.contract_testing.contractcase.contractcasekotlin.dsl.ContractCaseConfig
import io.contract_testing.contractcase.contractcasekotlin.dsl.ContractCaseDsl
import io.contract_testing.contractcase.contractcasekotlin.error.ContractCaseConfigurationError

internal fun ContractCaseConfig<*>.toBoundaryConfig(testRunId: String) = buildBoundaryConfig(testRunId) {}

internal fun <T> ContractCaseConfig<T>.toSuccessExample(testRunId: String) = buildBoundaryConfig(testRunId) {
    if (trigger != null) {
        if (testResponse != null) {
            triggerAndTest(trigger.toBoundaryTriggerSuccess(testResponse))
        } else {
            throw ContractCaseConfigurationError("Must specify `testResponse` if you are specifying a `trigger`");
        }
    } else {
        if (testResponse != null) {
            throw ContractCaseConfigurationError("Must specify `trigger` if you are specifying a `testResponse` function")
        }
    }
}

internal fun <T> ContractCaseConfig<T>.toFailingExample(testRunId: String) = buildBoundaryConfig(testRunId) {
    if (trigger != null) {
        if (testErrorResponse != null) {
            triggerAndTest(trigger.toBoundaryTriggerFailure(testErrorResponse))
        } else {
            throw ContractCaseConfigurationError("Must specify `testErrorResponse` if you are specifying a `trigger`");
        }
    } else {
        if (testErrorResponse != null) {
            throw ContractCaseConfigurationError("Must specify `trigger` if you are specifying a `testErrorResponse` function")
        }
    }
}

@ContractCaseDsl
private fun ContractCaseConfig<*>.buildBoundaryConfig(
    testRunId: String,
    additionalArgs: ContractCaseBoundaryConfig.Builder.() -> Unit
): ContractCaseBoundaryConfig =
    build(ContractCaseBoundaryConfig::builder) {
        testRunId(testRunId)
        providerName(providerName)
        consumerName(consumerName)
        brokerBaseUrl(brokerBaseUrl)
        logLevel(logLevel?.toString())
        contractDir(contractDir)
        contractFilename(contractFilename)
        printResults(printResults)
        throwOnFail(throwOnFail)
        publish(publish?.toString())
        brokerCiAccessToken(brokerCiAccessToken)
        brokerBasicAuth?.let { auth ->
            brokerBasicAuth(build(UserNamePassword::builder) {
                username(auth.username)
                password(auth.password)
            })
        }
        baseUrlUnderTest(baseUrlUnderTest)
        triggerAndTests(triggers?.toBoundaryTriggers() ?: emptyMap())
        stateHandlers(stateHandlers?.toBoundaryStateHandlers() ?: emptyMap())

        additionalArgs()
    }
