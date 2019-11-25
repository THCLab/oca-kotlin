package com.thehumancolossuslab.odca

import kotlinx.serialization.*
import kotlinx.serialization.json.*

class SchemasDeserializer(val schemasData: Array<HashMap<String, String>>) {
    fun call(): Array<Schema> {
        val json = Json(JsonConfiguration.Stable)
        val schemas: MutableList<Schema> = mutableListOf()

        for (schemaData in schemasData) {
            val schemaBase: SchemaBaseDto
            val schemaBaseJson = schemaData.remove("schemaBase")

            if (!schemaBaseJson.isNullOrBlank()) {
                schemaBase = json.parse(SchemaBaseDto.serializer(), schemaBaseJson)
            } else {
                throw Exception("schemaBase is missing")
            }

            val overlays: MutableMap<String, Any> = mutableMapOf()

            schemaData.forEach {
                when (it.key.split("-")[0]) {
                    "LabelOverlay" -> overlays.put(it.key, json.parse(LabelOverlayDto.serializer(), it.value))
                    "FormatOverlay" -> overlays.put(it.key, json.parse(FormatOverlayDto.serializer(), it.value))
                    "EntryOverlay" -> overlays.put(it.key, json.parse(EntryOverlayDto.serializer(), it.value))
                }
            }

            schemas.add(
                Schema(
                    SchemaDto(
                        schemaBase = schemaBase,
                        labelOverlays = overlays.filter { it.value is LabelOverlayDto }.mapValues { it.value as LabelOverlayDto },
                        formatOverlays = overlays.filter { it.value is FormatOverlayDto }.mapValues { it.value as FormatOverlayDto },
                        entryOverlays = overlays.filter { it.value is EntryOverlayDto }.mapValues { it.value as EntryOverlayDto }
                    )
                )
            )
        }

        return schemas.toTypedArray()
    }
}
