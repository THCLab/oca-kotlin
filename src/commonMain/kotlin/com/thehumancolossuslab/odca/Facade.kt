package com.thehumancolossuslab.odca

import kotlin.js.*

class Facade {
    @JsName("deserializeSchemas")
    fun deserializeSchemas(schemasData: Array<HashMap<String, String>>): Array<Schema> {
        return SchemasDeserializer(schemasData).call()
    }
}
