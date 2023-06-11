package io.contract_testing.contractcase.contractcasekotlin.dsl

typealias SetupFunction = () -> Map<String, Any?>?
typealias TeardownFunction = () -> Unit

class StateHandler private constructor(
    private val setupFn: SetupFunction,
    private val teardownFn: TeardownFunction
) {
    internal fun setup(): Map<String, Any?>? = setupFn()
    internal fun teardown() = teardownFn()

    class StateHandlerBuilder {

        // TODO: Support suspend functions in setup and teardown

        private var setupFn: SetupFunction = { mapOf() }
        private var teardownFn: TeardownFunction = {}

        /**
         * Add the state setup handler for a state that this test requires.
         *
         * @param setupHandler The setup state handler that is only invoked during the state setup lifecycle phase.
         */
        @ContractCaseDsl
        fun setup(setupHandler: SetupFunction) {
            setupFn = setupHandler
        }

        /**
         * Add the state teardown handler for a state that this test requires.
         *
         * @param teardownHandler The state teardown functions that is run after the Example has completed execution (see the
         *        [Example Lifecycle documentation](https://case.contract-testing.io/docs/defining-contracts/lifecycle)
         *        for more information).
         */
        @ContractCaseDsl
        fun teardown(teardownHandler: TeardownFunction) {
            teardownFn = teardownHandler
        }

        internal fun build(): StateHandler = StateHandler(setupFn = setupFn, teardownFn = teardownFn)

        internal fun build(setupHandler: SetupFunction): StateHandler =
            StateHandler(setupFn = setupHandler, teardownFn = teardownFn)
    }
}