import de.chojo.Repo

plugins {
    id("com.diffplug.spotless") version "6.20.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("de.chojo.publishdata") version "1.2.4"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    java
    `maven-publish`
}

group = "de.eldoria"
version = "1.1.0"

repositories {
    maven("https://eldonexus.de/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-proxies/")
}

dependencies {
    compileOnly("de.eldoria", "schematicbrushreborn-api", "2.5.4")
    compileOnly("org.spigotmc", "spigot-api", "1.14.4-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit", "worldedit-bukkit", "7.2.15")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
}

spotless {
    java {
        licenseHeaderFile(rootProject.file("HEADER.txt"))
        target("**/*.java")
    }
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_17
}

publishData {
    addBuildData()
    useEldoNexusRepos()
    publishComponent("java")
}

publishing {
    publications.create<MavenPublication>("maven") {
        publishData.configurePublication(this)
    }

    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    username = System.getenv("NEXUS_USERNAME")
                    password = System.getenv("NEXUS_PASSWORD")
                }
            }

            setUrl(publishData.getRepository())
            name = "EldoNexus"
        }
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    compileTestJava {
        options.encoding = "UTF-8"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    shadowJar {
        relocate("de.eldoria.eldoutilities", "de.eldoria.schematicbrush.libs.eldoutilities")
        relocate("de.eldoria.messageblocker", "de.eldoria.schematicbrush.libs.messageblocker")
        archiveBaseName.set("SchematicTools")
        mergeServiceFiles()
    }

    register<Copy>("copyToServer") {
        val path = project.property("targetDir") ?: "";
        if (path.toString().isEmpty()) {
            println("targetDir is not set in gradle properties")
            return@register
        }
        println("Copying jar to $path")
        from(shadowJar)
        destinationDir = File(path.toString())
    }

    build {
        dependsOn(shadowJar)
    }
}

bukkit {
    name = "SchematicTools"
    main = "de.eldoria.schematictools.SchematicTools"
    apiVersion = "1.16"
    authors = listOf("RainbowDashLabs")
    depend = listOf("SchematicBrushReborn")

    commands {
        register("schematictools") {
            description = "Base command of schematic tools"
            permission = "schematictools.use"
            aliases = listOf("sbt")
        }
    }
}
