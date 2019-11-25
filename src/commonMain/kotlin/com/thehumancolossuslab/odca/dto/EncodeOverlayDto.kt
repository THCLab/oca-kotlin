package com.thehumancolossuslab.odca

import kotlinx.serialization.*

@Serializable
data class EncodeOverlayDto(
    @SerialName("@context") val context: String = "https://odca.tech/overlays/v1",
    val type: String = "spec/overlay/encode/1.0",
    val description: String = "",
    @SerialName("issued_by") val issuedBy: String = "",
    val role: String,
    val purpose: String,
    @SerialName("schema_base") var schemaBaseId: String = "",
    @SerialName("default_encoding") val defaultEncoding: String = "utf-8",
    @SerialName("attr_encoding") val attrEncoding: Map<String, String> = mapOf()
)