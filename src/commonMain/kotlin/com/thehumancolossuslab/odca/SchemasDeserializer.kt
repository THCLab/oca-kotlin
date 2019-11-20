package com.thehumancolossuslab.odca

import kotlinx.serialization.*
import kotlinx.serialization.json.*

class SchemasDeserializer(val schemasData: Array<HashMap<String, String>>) {
    fun call(): Array<Schema> {
        val json = Json(JsonConfiguration.Stable)
        val schemas: MutableList<Schema> = mutableListOf()

        for (schemaData in schemasData) {
            val schemaBase: SchemaBase
            val schemaBaseJson = schemaData.remove("schemaBase")

            if (!schemaBaseJson.isNullOrBlank()) {
                schemaBase = json.parse(SchemaBase.serializer(), schemaBaseJson)
            } else {
                throw Exception("schemaBase is missing")
            }

            val overlays = schemaData.map {
                when (it.key.split("-")[0]) {
                    "LabelOverlay" -> json.parse(LabelOverlay.serializer(), it.value)
                    "FormatOverlay" -> json.parse(FormatOverlay.serializer(), it.value)
                    else -> null
                }
            }

            schemas.add(
                Schema(
                    schemaBase = schemaBase,
                    labelOverlays = overlays.filterIsInstance<LabelOverlay>().toMutableList(),
                    formatOverlays = overlays.filterIsInstance<FormatOverlay>().toMutableList()
                )
            )
        }

        return schemas.toTypedArray()
    }
}
