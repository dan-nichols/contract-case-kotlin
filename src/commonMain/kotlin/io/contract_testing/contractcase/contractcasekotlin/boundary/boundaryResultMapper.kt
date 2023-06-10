package io.contract_testing.contractcase.contractcasekotlin.boundary

import io.contract_testing.contractcase.case_boundary.BoundaryFailure
import io.contract_testing.contractcase.case_boundary.BoundaryFailureKindConstants
import io.contract_testing.contractcase.case_boundary.BoundaryResult
import io.contract_testing.contractcase.case_boundary.BoundaryResultTypeConstants
import io.contract_testing.contractcase.contractcasekotlin.error.ContractCaseConfigurationError
import io.contract_testing.contractcase.contractcasekotlin.error.ContractCaseCoreError
import io.contract_testing.contractcase.contractcasekotlin.error.ContractCaseExpectationsNotMet

internal fun BoundaryResult.verify() =
    when (resultType) {
        BoundaryResultTypeConstants.RESULT_SUCCESS -> Unit
        BoundaryResultTypeConstants.RESULT_FAILURE -> (this as BoundaryFailure).mapFailure()
        else -> Unit
    }

private fun BoundaryFailure.mapFailure(): Nothing =
    throw when (kind) {
        BoundaryFailureKindConstants.CASE_BROKER_ERROR,
        BoundaryFailureKindConstants.CASE_CONFIGURATION_ERROR,
        BoundaryFailureKindConstants.CASE_TRIGGER_ERROR -> ContractCaseConfigurationError(message, location)

        BoundaryFailureKindConstants.CASE_CORE_ERROR -> ContractCaseCoreError(message, location)

        BoundaryFailureKindConstants.CASE_FAILED_ASSERTION_ERROR,
        BoundaryFailureKindConstants.CASE_VERIFY_RETURN_ERROR -> ContractCaseExpectationsNotMet(message, location)

        else -> ContractCaseCoreError("Unhandled error kind ($kind): $message", location)
    }
