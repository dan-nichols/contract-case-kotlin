package io.contract_testing.contractcase.contractcasekotlin.error

class ContractCaseExpectationsNotMet(message: String, val location: String) : RuntimeException(message)
