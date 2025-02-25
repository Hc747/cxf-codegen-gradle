plugins {
    `java-platform`
    id("com.diffplug.spotless")
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform("org.junit:junit-bom:5.8.2"))
    api("org.assertj:assertj-core:3.22.0")
    api("commons-io:commons-io:2.11.0")
}
