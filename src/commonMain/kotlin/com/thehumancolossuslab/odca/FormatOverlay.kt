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
    @SerialName("schema_base") val schemaBaseId: String = "",
    @SerialName("attr_formats") val attrFormats: MutableMap<String, String> = mutableMapOf()
) {
    fun add(uuid: String, format: String) {
        attrFormats.put(uuid, format)
    }

    fun modify(uuid: String, format: String) {
        attrFormats.set(uuid, format)
    }

    fun delete(uuid: String) {
        attrFormats.remove(uuid)
    }
}