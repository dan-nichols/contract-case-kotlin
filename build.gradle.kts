plugins {
    alias(libs.plugins.multiplatform)
}

group = "io.contracttesting.contractcase"
version = "0.0.2" // x-release-please-version

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
                api(libs.bundles.case)

                implementation("com.diogonunes:JColor:5.5.1")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.bundles.kotlin.test)
                implementation(libs.bundles.ktor)
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
