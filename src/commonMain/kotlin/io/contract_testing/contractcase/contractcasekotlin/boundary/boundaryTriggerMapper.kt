package io.contract_testing.contractcase.contractcasekotlin.boundary

import io.contract_testing.contractcase.case_boundary.BoundarySuccess
import io.contract_testing.contractcase.case_boundary.ITriggerFunction
import io.contract_testing.contractcase.contractcasekotlin.dsl.TestErrorResponseFunction
import io.contract_testing.contractcase.contractcasekotlin.dsl.TestResponseFunction
import io.contract_testing.contractcase.contractcasekotlin.dsl.Trigger
import io.contract_testing.contractcase.contractcasekotlin.dsl.TriggerGroup
import io.contract_testing.contractcase.contractcasekotlin.dsl.TriggerGroups

internal fun TriggerGroups.toBoundaryTriggers(): Map<String, ITriggerFunction> =
    triggerGroups.fold(emptyMap()) { boundaryTriggers, group ->
        boundaryTriggers.plus(group.toBoundaryTrigger())
    }

internal fun <T> TriggerGroup<T>.toBoundaryTrigger(): Map<String, ITriggerFunction> =
    testResponses.toBoundaryResponseFunction(this, trigger::toBoundaryTriggerSuccess) +
            testErrorResponses.toBoundaryResponseFunction(this, trigger::toBoundaryTriggerFailure)

private fun <Function, T> Map<String, Function>.toBoundaryResponseFunction(
    group: TriggerGroup<T>,
    mapper: (Function) -> ITriggerFunction
): Map<String, ITriggerFunction> =
    entries.fold(emptyMap()) { aggregate, function ->
        aggregate.plus("${group.name}::${function.key}" to mapper(function.value))
    }

internal fun <T> Trigger<T>.toBoundaryTriggerSuccess(
    testResponseFunction: TestResponseFunction<T>
): ITriggerFunction =
    ITriggerFunction { config: Map<String, Any> ->
        val result = try {
            this(config)
        } catch (exception: Exception) {
            return@ITriggerFunction exception.toTriggerFailure()
        }

        try {
            testResponseFunction(result)
        } catch (exception: Exception) {
            return@ITriggerFunction exception.toVerifyFailure()
        }
        BoundarySuccess()
    }

internal fun <T> Trigger<T>.toBoundaryTriggerFailure(
    testErrorResponseFunction: TestErrorResponseFunction
): ITriggerFunction =
    ITriggerFunction { config: Map<String, Any> ->
        try {
            this(config)
            return@ITriggerFunction RuntimeException("Expected the trigger to fail, but it did not").toTriggerFailure()
        } catch (triggerException: Exception) {
            try {
                testErrorResponseFunction(triggerException)
            } catch (verifyException: Exception) {
                return@ITriggerFunction verifyException.toVerifyFailure()
            }
        }
        BoundarySuccess()
    }
