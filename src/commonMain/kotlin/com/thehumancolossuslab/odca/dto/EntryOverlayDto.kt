package com.thehumancolossuslab.odca

import kotlinx.serialization.*

@Serializable
data class EntryOverlayDto(
    @SerialName("@context") val context: String = "https://odca.tech/overlays/v1",
    val type: String = "spec/overlay/entry/1.0",
    @SerialName("issued_by") val issuedBy: String = "",
    val role: String,
    val purpose: String,
    @SerialName("schema_base") var schemaBaseId: String = "",
    val language: String,
    @SerialName("attr_entries") val attrEntries: Map<String, MutableList<String>> = mapOf()
)