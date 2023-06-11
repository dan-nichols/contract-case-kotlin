package io.contract_testing.contractcase.contractcasekotlin.dsl

import io.contract_testing.contractcase.contractcasekotlin.error.ContractCaseConfigurationError

/**
 * The config builder without trigger type
 */
@ContractCaseDsl
fun contractCaseConfig(options: ContractCaseConfig.ContractCaseConfigBuilder<Nothing>.() -> Unit): ContractCaseConfig<Nothing> =
    contractCaseConfigWithTrigger(options)

/**
 * The config builder when using trigger needs to explicitly define the trigger type
 */
@ContractCaseDsl
fun <T> contractCaseConfigWithTrigger(options: ContractCaseConfig.ContractCaseConfigBuilder<T>.() -> Unit): ContractCaseConfig<T> {
    val builder = ContractCaseConfig.ContractCaseConfigBuilder<T>()
    builder.options()

    // Check for property not initialised errors and throw this:
    // throw ContractCaseConfigurationError("`providerName` is required")

    return builder.build()
}

/**
 * Config object for ContractCase
 */
data class ContractCaseConfig<T> internal constructor(
    val providerName: String?,
    val consumerName: String?,
    val logLevel: LogLevel?,
    val contractDir: String?,
    val contractFilename: String?,
    val printResults: Boolean?,
    val throwOnFail: Boolean?,
    val publish: PublishType?,
    val brokerBaseUrl: String?,
    val brokerCiAccessToken: String?,
    val brokerBasicAuth: BrokerBasicAuthCredentials?,
    val baseUrlUnderTest: String?,
    val trigger: Trigger<T>?,
    val testResponse: TestResponseFunction<T>?,
    val testErrorResponse: TestErrorResponseFunction?,
    val triggers: TriggerGroups?,
    val stateHandlers: Map<String, StateHandler>?
) {

    class ContractCaseConfigBuilder<T> internal constructor() {

        /**
         * The name of the provider for this contract.
         */
        var providerName: String? = null

        /**
         * The name of the consumer for this contract.
         */
        var consumerName: String? = null

        /**
         * How much should we log during the test?
         */
        var logLevel: LogLevel? = null

        /**
         * The directory where the contract will be written. If you provide this, ContractCase will
         * generate the filename for you (unless `contractFilename` is specified, in which case this
         * setting is ignored)
         */
        var contractDir: String? = null

        /**
         * The filename where the contract will be written. If you provide this, `contractDir` is ignored
         */
        var contractFilename: String? = null

        /**
         * Whether results should be printed on standard out during the test run
         */
        var printResults: Boolean? = null

        /**
         * Whether the test should throw an error if the matching fails.
         *
         *
         * Note that any configuration errors will still fail the suite regardless of this setting. This
         * includes exceptions thrown during trigger functions, but does not include exceptions thrown by
         * testResponse functions.
         *
         *
         * Default: `true` in contract definition, `false` in contract verification
         */
        var throwOnFail: Boolean? = null

        /**
         * Whether to publish contracts or verification results to the broker
         *
         * @see PublishType
         */
        var publish: PublishType? = null

        /**
         * The base URL for the contract broker
         */
        var brokerBaseUrl: String? = null

        /**
         * The access token to use for the contract broker. Must have CI scope.
         *
         *
         * If this is specified along with brokerBasicAuth, the basic auth is ignored.
         */
        var brokerCiAccessToken: String? = null

        /**
         * The basic authentication username and password to access the contract broker.
         *
         *
         * If this is specified along with brokerCiAccessToken, basic auth credentials are ignored.
         */
        var brokerBasicAuth: BrokerBasicAuthCredentials? = null

        /**
         * The base URL for your real server, if you are testing a http server.
         *
         */
        @Deprecated("This will be moved to a config property that allows configuration for arbitrary mocks")
        var baseUrlUnderTest: String? = null

        var trigger: Trigger<T>? = null
        var testResponse: TestResponseFunction<T>? = null
        var testErrorResponse: TestErrorResponseFunction? = null

        var triggers: TriggerGroups? = null

        /**
         * State setup and teardown handlers for any states this test requires (see
         * [writing state handlers](https://case.contract-testing.io/docs/reference/state-handlers/) for more details).
         *
         * Please use `stateHandler` and `stateSetup` to add handlers to this map.
         */
        private var stateHandlers: Map<String, StateHandler> = mutableMapOf()

        /**
         * Add a state setup and/or teardown handler for a state that this test requires (see
         * [writing state handlers](https://case.contract-testing.io/docs/reference/state-handlers/) for more details).
         *
         * @param stateName The name that the state handler will be keyed to.
         * @param handlers DSL context for setup and teardown handler definitions.
         */
        @ContractCaseDsl
        fun stateHandler(stateName: String, handlers: StateHandler.StateHandlerBuilder.() -> Unit) {
            val builder = StateHandler.StateHandlerBuilder()
            builder.handlers()
            stateHandlers += stateName to builder.build()
        }

        /**
         * TODO: **Open to naming suggestions on this one. Cannot overload `stateHandler` since the callback
         *       argument is ambiguous.**
         *
         * Add a state setup handler for a state that this test requires (see
         * [writing state handlers](https://case.contract-testing.io/docs/reference/state-handlers/) for more details).
         *
         * @param stateName The name that the state handler will be keyed to.
         * @param setupHandler The setup state handler that is only invoked during the state setup lifecycle phase.
         */
        @ContractCaseDsl
        fun stateSetupHandler(stateName: String, setupHandler: SetupFunction) {
            val builder = StateHandler.StateHandlerBuilder()
            stateHandlers += stateName to builder.build(setupHandler)
        }

        fun build(): ContractCaseConfig<T> =
            ContractCaseConfig(
                providerName = providerName,
                consumerName = consumerName,
                logLevel = logLevel,
                contractDir = contractDir,
                contractFilename = contractFilename,
                printResults = printResults,
                throwOnFail = throwOnFail,
                publish = publish,
                brokerBaseUrl = brokerBaseUrl,
                brokerCiAccessToken = brokerCiAccessToken,
                brokerBasicAuth = brokerBasicAuth,
                baseUrlUnderTest = baseUrlUnderTest,
                trigger = trigger,
                testErrorResponse = testErrorResponse,
                testResponse = testResponse,
                triggers = triggers,
                stateHandlers = stateHandlers,
            )
    }

    /**
     * `This` receiver takes precedence in merge over `parent` argument.
     */
    internal fun mergeConfig(parent: ContractCaseConfig<*>): ContractCaseConfig<T> {
        val mergedTrigger: Trigger<T>? = try {
            @Suppress("UNCHECKED_CAST") // We are manually checking the cast with the try/catch block
            (trigger ?: parent.trigger) as Trigger<T>?
        } catch (exception: ClassCastException) {
            {
                throw ContractCaseConfigurationError(
                    "`trigger` return type defined in `additionalConfig` does not match type defined in parent configuration."
                )
            }
        }
        val mergedTestResponseFunction: TestResponseFunction<T>? = try {
            @Suppress("UNCHECKED_CAST") // We are manually checking the cast with the try/catch block
            (testResponse ?: parent.testResponse) as TestResponseFunction<T>?
        } catch (exception: ClassCastException) {
            {
                // TODO: Find a way to test this shit
                throw ContractCaseConfigurationError(
                    "`testResponse` Function argument type defined in `additionalConfig` does not match type defined in parent configuration."
                )
            }
        }

        return ContractCaseConfig(
            providerName = this.providerName ?: parent.providerName,
            consumerName = consumerName ?: parent.consumerName,
            logLevel = logLevel ?: parent.logLevel,
            contractDir = contractDir ?: parent.contractDir,
            contractFilename = contractFilename ?: parent.contractFilename,
            printResults = printResults ?: parent.printResults,
            throwOnFail = throwOnFail ?: parent.throwOnFail,
            publish = publish ?: parent.publish,
            brokerBaseUrl = brokerBaseUrl ?: parent.brokerBaseUrl,
            brokerCiAccessToken = brokerCiAccessToken ?: parent.brokerCiAccessToken,
            brokerBasicAuth = brokerBasicAuth ?: parent.brokerBasicAuth,
            baseUrlUnderTest = baseUrlUnderTest ?: parent.baseUrlUnderTest,
            trigger = mergedTrigger,
            testResponse = mergedTestResponseFunction,
            testErrorResponse = testErrorResponse ?: parent.testErrorResponse,
            triggers = triggers ?: parent.triggers,
            stateHandlers = stateHandlers ?: parent.stateHandlers
        )
    }
}
