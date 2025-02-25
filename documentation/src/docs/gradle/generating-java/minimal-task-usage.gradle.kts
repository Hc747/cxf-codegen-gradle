// tag::code[]
import io.mateo.cxf.codegen.wsdl2java.Wsdl2Java
// end::code[]

plugins {
    java
    id("io.mateo.cxf-codegen")
}

// tag::code[]

// ...

tasks.register("example", Wsdl2Java::class) { // <1>
    toolOptions { // <2>
        wsdl.set(file("path/to/example.wsdl")) // <3>
    }
    allJvmArgs = listOf("-Duser.language=fr", "-Duser.country=CA") // <4>
}
// end::code[]

tasks.register("verify") {
    doLast {
        println(tasks.getByName("example", Wsdl2Java::class).wsdl2JavaOptions.wsdl.get())
    }
}
