package com.thehumancolossuslab.oca

import kotlinx.serialization.*

@Serializable
data class SchemaBaseDto(
    @SerialName("@context") val context: String = "https://odca.tech/v1",
    val name: String,
    val type: String = "spec/schema_base/1.0",
    val description: String,
    val classification: String,
    @SerialName("issued_by") val issuedBy: String = "",
    val attributes: Map<String, String> = mapOf(),
    @SerialName("pii_attributes") val piiAttributes: List<String> = listOf()
)