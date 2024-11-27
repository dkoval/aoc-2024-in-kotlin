plugins {
    kotlin("jvm") version "2.0.21"
}

kotlin {
    // updates the toolchain for Java compile tasks as well
    jvmToolchain(21)
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11"
    }
}
