package io.contract_testing.contractcase.contractcasekotlin.dsl

import io.contract_testing.contractcase.case_boundary.ConfigPublishConstants

/**
 * Enum for the config option to control whether ContractCase will publish results.
 */
enum class PublishType(private val value: String) {
    /**
     * Always publish contracts and verification statuses (not recommended, as it is not good practice
     * to publish from developer machines during routine test runs)
     */
    ALWAYS(ConfigPublishConstants.ALWAYS),

    /**
     * Don't publish contracts or verification statuses during this run
     */
    NEVER(ConfigPublishConstants.NEVER),

    /**
     * Only publish contracts or verification statuses when in CI according to
     * [ci-info](https://github.com/watson/ci-info#supported-ci-tools)
     */
    ONLY_IN_CI(ConfigPublishConstants.ONLY_IN_CI);

    override fun toString(): String = value
}
