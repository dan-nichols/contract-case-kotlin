plugins {
    alias(libs.plugins.multiplatform)
}

group = "io.contracttesting.contractcase"
version = "0.0.1" // x-release-please-version

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.case.mock.types)
                api(libs.case.matchers)
                api(libs.case.boundary)

                implementation("com.diogonunes:JColor:5.5.1")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.bundles.kotlin.test)
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
