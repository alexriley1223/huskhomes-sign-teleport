plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    id("com.gradleup.shadow") version "9.6.1"
    id("xyz.jpenilla.run-paper") version "3.1.0"
}

group = "com.alexriley.me"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")

    maven {
        url = uri("https://repo.william278.net/releases")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.3.build.+")
    compileOnly("net.william278.huskhomes:huskhomes-bukkit:4.11.1")
    implementation(libs.kotlin.stdlib)
}

kotlin {
    jvmToolchain(25)
}

tasks {
    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        // Relocate Kotlin stdlib to prevent conflicts with other plugins
        relocate("kotlin", "${project.group}.libs.kotlin")
    }

    build {
        dependsOn(shadowJar)
    }

    runServer {
        minecraftVersion("26.3")
        pluginJars.from(shadowJar.flatMap { it.archiveFile })

        downloadPlugins {
            modrinth("huskhomes", "4.11.1")
        }
    }
}
