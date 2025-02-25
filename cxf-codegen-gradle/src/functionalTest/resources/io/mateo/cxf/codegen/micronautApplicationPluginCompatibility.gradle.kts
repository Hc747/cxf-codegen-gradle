import io.mateo.cxf.codegen.wsdl2java.Wsdl2Java

plugins {
    id("io.mateo.cxf-codegen")
    id("io.micronaut.application") version "3.1.1"
}

repositories {
    mavenCentral()
}

application {
    // This does not need to exist for tests purposes.
    mainClass.set("io.mateo.Main")
}

dependencies {
    cxfCodegen("jakarta.xml.ws:jakarta.xml.ws-api:2.3.3")
    cxfCodegen("jakarta.annotation:jakarta.annotation-api:1.3.5")
}

tasks.register("calculator", Wsdl2Java::class) {
    toolOptions {
        wsdl.set(file("wsdls/calculator.wsdl"))
    }
}
