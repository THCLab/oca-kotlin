package com.thehumancolossuslab.odca

import kotlin.js.JsName
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Data(val schemaBase: Int)

data class AClass(val input: String) {

    @JsName("schemaBase")
    fun schemaBase(): String {
        val json = Json(JsonConfiguration.Stable)
        val jsonData = json.stringify(Data.serializer(), Data(42))
        return jsonData;
    }
}
