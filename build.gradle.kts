import org.gradle.api.tasks.wrapper.Wrapper.DistributionType

plugins {
    kotlin("jvm") version "1.8.0-Beta"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "7.6"
        distributionType = DistributionType.ALL
    }

    compileKotlin {
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.ExperimentalStdlibApi"
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.time.ExperimentalTime"
    }
}

