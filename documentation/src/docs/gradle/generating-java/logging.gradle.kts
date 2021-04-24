plugins {
    java
    id("io.mateo.cxf-codegen")
}

repositories {
    mavenCentral()
}

// tag::code[]
dependencies {
    cxfCodegen("ch.qos.logback:logback-classic:1.2.3")
}
// end::code[]

dependencies {
    cxfCodegen("jakarta.xml.ws:jakarta.xml.ws-api:2.3.3")
    cxfCodegen("jakarta.annotation:jakarta.annotation-api:1.3.5")
}

cxfCodegen {
    wsdl2java {
        register("example") {
            wsdl.set(file("wsdls/calculator.wsdl"))
        }
    }
}
