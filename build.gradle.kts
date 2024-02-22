plugins {
    kotlin("jvm")
    id("fabric-loom")
    `maven-publish`
    java
}

group = property("maven_group")!!
version = property("mod_version")!!

sourceSets{
    main{
        resources{
            srcDirs("src/main/generated")//, "src/main/resources")
        }
    }
}

loom{
    runs{

        create("Data Generation"){

            client()
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/main/generated")}")
            vmArg("-Dfabric-api.datagen.strict_validation")
            vmArg("-Dfabric-api.datagen.modid=${project.property("modid")}")



            ideConfigGenerated(true)
            runDir = "build/datagen"
        }
    }
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
    maven{setUrl("https://maven.shedaniel.me/")}
    maven{setUrl("https://maven.terraformersmc.com/")}
    maven{setUrl("https://maven.blamejared.com")}
    maven{setUrl("https://api.modrinth.com/maven")}
    maven{setUrl("https://mvn.devos.one/snapshots/")}
    maven{setUrl("https://maven.cafeteria.dev/releases")}
    maven{setUrl("https://jitpack.io/")}
    maven{setUrl("https://maven.jamieswhiteshirt.com/libs-release")}
    maven{setUrl("https://cursemaven.com")}
    maven{setUrl("https://maven.cafeteria.dev/releases")}
    maven{setUrl("https://maven.tterrag.com/");content { includeGroup("com.jozufozu.flywheel") }}
    maven{setUrl("https://maven.ladysnake.org/releases")}
    //maven{setUrl("")}
}

dependencies {
    implementation ("org.jetbrains:annotations:24.0.1")
    implementation("org.jgrapht:jgrapht-core:1.5.1")
    implementation("org.jheaps:jheaps:0.13")
    compileOnly ("com.demonwav.mcdev:annotations:1.0")

    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")

    modImplementation("maven.modrinth:Spectrum:${property("spectrum_version")}")

    modImplementation("com.simibubi.create:create-fabric-1.20.1:${property("create_fabric_version")}"){
        exclude("dev.emi:trinkets")
        exclude("net.fabricmc.fabric-api:fabric-api")
    }

    modImplementation("com.github.Noaaan:Matchbooks:${property("matchbooks_version")}")
    modImplementation("maven.modrinth:Revelationary:${property("revelationary_version")}")

    modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${property("cardinal_components_version")}")
    modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-scoreboard:${property("cardinal_components_version")}")
    modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-level:${property("cardinal_components_version")}")
    include("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${property("cardinal_components_version")}")
    include("dev.onyxstudios.cardinal-components-api:cardinal-components-scoreboard:${property("cardinal_components_version")}")
    include("dev.onyxstudios.cardinal-components-api:cardinal-components-level:${property("cardinal_components_version")}")

    modApi("me.shedaniel.cloth:cloth-config-fabric:${property("cloth_config_version")}") { exclude("net.fabricmc.fabric-api") }
    modApi("dev.architectury:architectury-fabric:${property("architectury_version")}") { exclude("net.fabricmc.fabric-api") }
    modApi("com.terraformersmc:modmenu:${property("modmenu_version")}")
    modImplementation("vazkii.patchouli:Patchouli:${property("patchouli_version")}")
    modImplementation("dev.emi:trinkets:${property("trinkets_version")}")


    modImplementation("maven.modrinth:AdditionalEntityAttributes:${property("additional_entity_attributes_version")}")
    modImplementation("com.github.DaFuqs:Arrowhead:${property("arrowhead_version")}")
    modImplementation("maven.modrinth:fractal-lib:${property("fractal_version")}")
    modImplementation("com.github.DaFuqs:DimensionalReverb:${property("dimensional_reverb_version")}")

    modRuntimeOnly("me.shedaniel:RoughlyEnoughItems-fabric:${property("rei_version")}")

}



tasks {

    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        from("LICENSE")
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                artifact(remapJar) {
                    builtBy(remapJar)
                }
                artifact(kotlinSourcesJar) {
                    builtBy(remapSourcesJar)
                }
            }
        }

        // select the repositories you want to publish to
        repositories {
            // uncomment to publish to the local maven
            // mavenLocal()
        }
    }

    compileJava {
        targetCompatibility = "17"
        sourceCompatibility = "17"
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.freeCompilerArgs+="-Xcontext-receivers"
    }

}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}



// configure the maven publication
