package io.contract_testing.contractcase.contractcasekotlin.error

class ContractCaseCoreError(message: String, val location: String) : RuntimeException(message)
