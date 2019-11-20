package com.thehumancolossuslab.odca

import kotlinx.serialization.*

@Serializable
data class FormatOverlay(
    @SerialName("@context") val context: String,
    val type: String,
    val description: String,
    @SerialName("issued_by") val issuedBy: String,
    val role: String,
    val purpose: String,
    @SerialName("schema_base") val schemaBaseId: String,
    @SerialName("attr_formats") val attrFormats: MutableMap<String, String>
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