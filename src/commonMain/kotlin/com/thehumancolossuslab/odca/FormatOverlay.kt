package com.thehumancolossuslab.odca

import kotlinx.serialization.*

@Serializable
data class FormatOverlay(
    @SerialName("@context") val context: String = "https://odca.tech/overlays/v1",
    val type: String = "spec/overlay/format/1.0",
    val description: String = "",
    @SerialName("issued_by") val issuedBy: String = "",
    val role: String,
    val purpose: String,
    @SerialName("schema_base") var schemaBaseId: String,
    @SerialName("attr_formats") val attrFormats: MutableMap<String, String> = mutableMapOf()
) {
    fun add(attribute: AttributeDto) {
        if (attribute.format == null) { throw Exception() }
        attrFormats.put(attribute.uuid, attribute.format)
    }

    fun modify(uuid: String, attribute: AttributeDto) {
        if (attribute.format == null) { throw Exception() }
        attrFormats.set(uuid, attribute.format)
    }

    fun delete(uuid: String) {
        attrFormats.remove(uuid)
    }
}