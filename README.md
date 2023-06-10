# ContractCase Contract Testing Framework - Kotlin DSL

[![Build and test](https://github.com/dan-nichols/contract-case-kotlin/actions/workflows/build-and-test.yml/badge.svg?branch=main)](https://github.com/case-contract-testing/kotlin-dsl/actions/workflows/build-and-test.yml)

<span align="center">

![Case](https://github.com/case-contract-testing/contract-case/raw/main/docs/suitcase.png)

<sub>[Briefcase sticker created by Gohsantosadrive on Flaticon](https://www.flaticon.com/free-stickers/law)</sub>

</span>

These are the Kotlin bindings for the [ContractCase contract testing framework](https://case.contract-testing.io/).
It exists for making the use of ContractCase in idiomatic Kotlin.

Since [Jsii does not currently support native Kotlin](https://github.com/aws/jsii/issues/1541), this DSL also only supports JVM target for Kotlin. Other multiplatform targets may be supported in the future if implemented by AWS. If there is enough interest, we may decide to map `expect/actual` implementations directly here.

Read the [documentation here](https://case.contract-testing.io/docs/intro/).

## Installing

<!-- x-release-please-start-version -->
_build.gradle_:
```groovy
testImplementation "io.contract-testing.contractcase:contract-case-kotlin:0.0.1"
```
<!-- x-release-please-end -->
