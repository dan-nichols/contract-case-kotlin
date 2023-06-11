package io.contract_testing.contractcase.contractcasekotlin.dsl

/**
 * Defines credentials for brokers secured with HTTP basic auth
 *
 * @param username The basic auth username
 * @param password The basic auth password
 */
data class BrokerBasicAuthCredentials(val username: String, val password: String)
