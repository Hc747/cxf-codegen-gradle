plugins {
    `java-gradle-plugin`
    `java-library-conventions`
    `maven-publish`
    signing
    id("com.gradle.plugin-publish")
}

description = "CXF Codegen"

val pluginName = "cxfCodegen"

gradlePlugin {
    plugins {
        create(pluginName) {
            id = "io.mateo.cxf-codegen"
            displayName = "CXF Codegen"
            description = "Plugin to generate code sources from WSDL."
            implementationClass = "io.mateo.cxf.codegen.CxfCodegenPlugin"
        }
    }
}

testing {
    suites {
        register("functionalTest", JvmTestSuite::class) {
            dependencies {
                implementation(project)
                implementation("commons-io:commons-io")
            }
            targets.configureEach {
                testTask.configure {
                    testLogging {
                        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                    }
                    // TODO: Drop support for older versions of Gradle.
                    systemProperty("gradle5", System.getProperty("gradle5", false.toString()))
                    systemProperty("gradle6", System.getProperty("gradle6", false.toString()))
                    shouldRunAfter(named("test"))
                }
            }
        }
    }
}

pluginBundle {
    website = "https://github.com/ciscoo/cxf-codegen-gradle"
    vcsUrl = "https://github.com/ciscoo/cxf-codegen-gradle"
    description = "Gradle plugin port of the Maven CXF Codegen plugin."
    (plugins) {
        named(pluginName) {
            displayName = "CXF Codegen plugin"
            description = "Gradle port of the Apache CXF Code Generation Maven2 plugin (cxf-codegen-plugin)."
            tags = listOf("cxf", "wsdl2java")
        }
    }
}

gradlePlugin.testSourceSets(sourceSets["functionalTest"])

tasks {
    check {
        dependsOn(testing.suites.named("functionalTest"))
    }
    publishPlugins {
        dependsOn(build)
    }
    withType<Jar>().configureEach {
        manifest.attributes["Automatic-Module-Name"] = "io.mateo.cxf.codegen"
    }
    javadoc {
        options {
            header = "CXF Codegen"
            windowTitle = "CXF Codegen Gradle Plugin $version API"
            this as StandardJavadocDocletOptions
            docTitle = "CXF Codegen Gradle Plugin $version API"
        }
    }
}
val isSnapshot = project.version.toString().endsWith("SNAPSHOT")

publishing {
    repositories {
        maven {
            name = "sonatype"
            url = if (isSnapshot) {
                uri("https://oss.sonatype.org/content/repositories/snapshots")
            } else {
                uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
            credentials(PasswordCredentials::class)
        }
    }
    publications.containerWithType(MavenPublication::class).configureEach {
        pom {
            url.set("https://github.com/ciscoo/cxf-codegen-gradle")
            licenses {
                name.set("Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0")
            }
            developers {
                developer {
                    name.set("Francisco Mateo")
                    email.set("cisco21c@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:github.com/ciscoo/cxf-codegen-gradle.git")
                developerConnection.set("scm:git:ssh://github.com/ciscoo/cxf-codegen-gradle.git")
                url.set("https://github.com/ciscoo/cxf-codegen-gradle")
            }
            issueManagement {
                system.set("GitHub")
                url.set("https://github.com/ciscoo/cxf-codegen-gradle/issues")
            }
        }
    }
}

val isCIEnvironment = System.getenv("CI")?.toBoolean() ?: false

signing {
    sign(publishing.publications)
    isRequired = !(isSnapshot || isCIEnvironment)
}

afterEvaluate {
    publishing {
        publications.named<MavenPublication>("pluginMaven") {
            pom {
                name.set("CXF Codegen Gradle Plugin")
                description.set("Plugin to generate code sources from WSDL.")
            }
        }
        publications.named<MavenPublication>("cxfCodegenPluginMarkerMaven") {
            pom {
                name.set("CXF Codegen Gradle Plugin Marker")
                description.set("CXF Codegen Gradle plugin marker artifact.")
            }
        }
    }
}
