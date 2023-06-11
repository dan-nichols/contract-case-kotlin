package io.contract_testing.contractcase.contractcasekotlin.boundary

import io.contract_testing.contractcase.contractcasekotlin.dsl.StateHandler
import io.contract_testing.contractcase.case_boundary.BoundaryResult
import io.contract_testing.contractcase.case_boundary.BoundaryStateHandler
import io.contract_testing.contractcase.case_boundary.BoundaryStateHandlerWithTeardown
import io.contract_testing.contractcase.case_boundary.BoundarySuccess
import io.contract_testing.contractcase.case_boundary.BoundarySuccessWithMap

internal fun Map<String, StateHandler>.toBoundaryStateHandlers(): Map<String, BoundaryStateHandler> =
    mapValues { (_, handler) -> handler.toBoundaryStateHandler() }

private fun StateHandler.toBoundaryStateHandler(): BoundaryStateHandlerWithTeardown =
    object : BoundaryStateHandlerWithTeardown() {
        override fun setup(): BoundaryResult {
            try {
                val config = this@toBoundaryStateHandler.setup() ?: return BoundarySuccess()
                return BoundarySuccessWithMap(config)
            } catch (exception: Exception) {
                return exception.toStateFailure()
            }
        }

        override fun teardown(): BoundaryResult = try {
            this@toBoundaryStateHandler.teardown()
            BoundarySuccess()
        } catch (exception: Exception) {
            exception.toStateFailure()
        }
    }
