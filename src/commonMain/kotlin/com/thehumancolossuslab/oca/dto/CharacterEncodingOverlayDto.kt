package com.thehumancolossuslab.oca

import kotlinx.serialization.*

@Serializable
data class CharacterEncodingOverlayDto(
    @SerialName("@context") val context: String = "https://odca.tech/overlays/v1",
    val type: String = "spec/overlay/character_encoding/1.0",
    @SerialName("issued_by") val issuedBy: String = "",
    val role: String,
    val purpose: String,
    @SerialName("schema_base") var schemaBaseId: String = "",
    @SerialName("default_character_encoding") val defaultCharacterEncoding: String = "utf-8",
    @SerialName("attr_character_encoding") val attrCharacterEncoding: Map<String, String> = mapOf()
)