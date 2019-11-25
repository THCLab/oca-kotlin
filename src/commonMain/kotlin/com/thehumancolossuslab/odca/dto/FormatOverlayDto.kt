package com.thehumancolossuslab.odca

import kotlinx.serialization.*

@Serializable
data class FormatOverlayDto(
    @SerialName("@context") val context: String = "https://odca.tech/overlays/v1",
    val type: String = "spec/overlay/format/1.0",
    val description: String = "",
    @SerialName("issued_by") val issuedBy: String = "",
    val role: String,
    val purpose: String,
    @SerialName("schema_base") var schemaBaseId: String = "",
    @SerialName("attr_formats") val attrFormats: Map<String, String> = mapOf()
)