package io.contract_testing.contractcase.contractcasekotlin.error

class ContractCaseConfigurationError : RuntimeException {
    val location: String

    constructor(message: String) : super(message) {
        location = Thread.currentThread().stackTrace.joinToString { obj: StackTraceElement -> obj.toString() }
    }

    constructor(message: String, location: String) : super(message) {
        this.location = location
    }
}
