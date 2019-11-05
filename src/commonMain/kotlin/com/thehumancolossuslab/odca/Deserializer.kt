package com.thehumancolossuslab.odca

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.js.JsName

@Serializable
data class SchemaBase(
    @SerialName("@context") val context: String,
    val name: String,
    val type: String,
    val description: String,
    val classification: String,
    @SerialName("issued_by") val issuedBy: String,
    val attributes: Map<String, String>,
    @SerialName("pii_attributes") val piiAttributes: List<String>
    )

@Serializable
data class LabelOverlay(
    @SerialName("@context") val context: String,
    val type: String,
    val description: String,
    @SerialName("issued_by") val issuedBy: String,
    val role: String,
    val purpose: String,
    @SerialName("schema_base") val schemaBaseId: String,
    val language: String,
    @SerialName("attr_labels") val attrLabels: Map<String, String>,
    @SerialName("attr_categories") val attrCategories: List<String>,
    @SerialName("category_labels") val categoryLabels: Map<String, String>,
    @SerialName("category_attributes") val categoryAttributes: Map<String, List<String>>
    )

@Serializable
data class FormatOverlay(
    @SerialName("@context") val context: String,
    val type: String,
    val description: String,
    @SerialName("issued_by") val issuedBy: String,
    val role: String,
    val purpose: String,
    @SerialName("schema_base") val schemaBaseId: String,
    @SerialName("attr_formats") val attrFormats: Map<String, String>
    )

@Serializable
data class Schema(
    val schemaBase: SchemaBase,
    val labelOverlays: List<LabelOverlay>,
    val formatOverlays: List<FormatOverlay>
)

data class Deserializer(val input: String) {

    @JsName("call")
    fun call(): String {
        return Json.stringify(Schema.serializer(), execute())
    }

    fun execute(): Schema {
        val json = Json(JsonConfiguration.Stable)
        val schemaData = json.parse(JsonObjectSerializer, input)

        val objs = schemaData.map {
            when (it.key.split("-")[0]) {
                "schemaBase" -> json.parse(SchemaBase.serializer(), it.value.toString())
                "LabelOverlay" -> json.parse(LabelOverlay.serializer(), it.value.toString())
                "FormatOverlay" -> json.parse(FormatOverlay.serializer(), it.value.toString())
                else -> null
            }
        }

        val schema = Schema(
            objs.filterIsInstance<SchemaBase>()[0],
            objs.filterIsInstance<LabelOverlay>(),
            objs.filterIsInstance<FormatOverlay>()
        )

        return schema;
    }
}
