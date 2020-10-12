package com.thehumancolossuslab.oca

import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlin.js.*

class Facade {
    @JsName("deserializeSchemas")
    fun deserializeSchemas(schemasData: Array<HashMap<String, String>>): Array<Schema> {
        return SchemasDeserializer(schemasData).call()
    }

    @ExperimentalUnsignedTypes
    @UnstableDefault
    @JsName("renderSchema")
    fun renderSchema(baseInfo: HashMap<String, String>, attributes: Array<AttributeDto>): Schema {
        return SchemaRenderer(baseInfo, attributes).call()
    }

    @ExperimentalUnsignedTypes
    @UnstableDefault
    @JsName("serialize")
    fun serialize(schema: Schema): String {
        return Json.stringify(SchemaDto.serializer(), schema.toDto())
    }
}
