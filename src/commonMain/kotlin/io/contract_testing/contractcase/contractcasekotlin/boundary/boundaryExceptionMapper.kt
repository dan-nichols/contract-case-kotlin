package io.contract_testing.contractcase.contractcasekotlin.boundary

import io.contract_testing.contractcase.case_boundary.BoundaryFailure
import io.contract_testing.contractcase.case_boundary.BoundaryFailureKindConstants

internal fun Exception.toBoundaryFailure(): BoundaryFailure =
    BoundaryFailure(javaClass.name, message!!, stackTraceToString())

internal fun Exception.toTriggerFailure(): BoundaryFailure =
    BoundaryFailure(BoundaryFailureKindConstants.CASE_TRIGGER_ERROR, message!!, stackTraceToString())

internal fun Exception.toVerifyFailure(): BoundaryFailure =
    BoundaryFailure(BoundaryFailureKindConstants.CASE_VERIFY_RETURN_ERROR, message!!, stackTraceToString())

internal fun Exception.toStateFailure(): BoundaryFailure =
    BoundaryFailure(
        BoundaryFailureKindConstants.CASE_CONFIGURATION_ERROR,
        "State handler failed with $message",
        stackTraceToString()
    )
