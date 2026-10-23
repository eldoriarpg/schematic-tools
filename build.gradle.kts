import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    `maven-publish`
    alias(libs.plugins.spotless)
    alias(libs.plugins.publishdata)
    alias(libs.plugins.publishdata.manifest)
    alias(libs.plugins.shadow)
    alias(libs.plugins.pluginyml.bukkit)
    alias(libs.plugins.runserver)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-proxies/")
}

dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.schematicbrushreborn.api)
    compileOnly(libs.bundles.utilities)
    compileOnly(platform(libs.jackson.bom))
    compileOnly(libs.bundles.jackson)
    compileOnly(libs.fawe.core) {
        exclude("com.intellectualsites.paster")
        exclude("net.kyori")
    }
    compileOnly(libs.fawe.bukkit) {
        exclude("net.kyori")
    }

    testImplementation(libs.paper)
    testImplementation(libs.schematicbrushreborn.api)
    testImplementation(libs.bundles.utilities)
    testImplementation(platform(libs.jackson.bom))
    testImplementation(libs.bundles.jackson)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
    withSourcesJar()
    withJavadocJar()
}

spotless {
    java {
        target("src/**/*.java")
        licenseHeaderFile(rootProject.file("HEADER.txt"))
        trimTrailingWhitespace()
        endWithNewline()
        palantirJavaFormat(libs.versions.palantir.get()).formatJavadoc(false)
        removeUnusedImports()
        importOrder("", "java", "javax", "\\#")
        encoding("UTF-8")
    }
}

publishData {
    eldoNexus(dev = false)
    component = "shadow"
    buildData {
        put("lynaProduct", "6")
    }
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            url.set("https://github.com/eldoriarpg/schematic-tools")
            developers {
                developer {
                    name.set("Nora Fülling")
                    organization.set("EldoriaRPG")
                    organizationUrl.set("https://github.com/eldoriarpg")
                }
            }
            licenses {
                license {
                    name.set("GNU Affero General Public License v3.0")
                    url.set("https://github.com/eldoriarpg/schematic-tools/blob/main/LICENSE.md")
                }
            }
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
        dependsOn(spotlessCheck)
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    shadowJar {
        val shadebase = "de.eldoria.schematicbrush.libs."
        relocate("com.fasterxml", shadebase + "fasterxml")
        relocate("tools.jackson", shadebase + "jackson")
        relocate("dev.chojo.ocular", shadebase + "ocular")
        relocate("de.eldoria.jacksonbukkit", shadebase + "jacksonbukkit")
        relocate("de.eldoria.eldoutilities", shadebase + "utilities")
        archiveBaseName.set("SchematicTools")
        mergeServiceFiles()
    }

    build {
        dependsOn(shadowJar)
    }

    runServer {
        minecraftVersion("26.3")
        downloadPlugins {
            modrinth("fastasyncworldedit", "2.16.1")
            modrinth("luckperms", "v5.5.71-bukkit")
            // TODO: download Schematic Brush Reborn 3 once it has a stable download URL
        }

        jvmArgs("-Dcom.mojang.eula.agree=true")
    }
}

pluginManifest {
    platforms("paper")
    requires("SchematicBrushReborn") {
        spigot(98499)
        link("https://lyna.eldoria.de/products/1")
    }
    requires("FastAsyncWorldEdit") {
        modrinth("fastasyncworldedit")
        hangar("IntellectualSites/FastAsyncWorldEdit")
    }
}

bukkit {
    name = "SchematicTools"
    version = project.version.toString()
    main = "de.eldoria.schematictools.SchematicTools"
    apiVersion = "1.21.11"
    authors = listOf("RainbowDashLabs")
    depend = listOf("SchematicBrushReborn", "FastAsyncWorldEdit")

    commands {
        register("schematictools") {
            description = "Base command of schematic tools"
            permission = "schematictools.use"
            aliases = listOf("sbt")
        }
    }

    permissions {
        register("schematictools.updates") {
            description = "Hear about new versions on join and download them"
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}
