package com.thehumancolossuslab.odca

import kotlinx.serialization.*

@Serializable
data class SchemaBaseDto(
    @SerialName("@context") val context: String,
    val name: String,
    val type: String,
    val description: String,
    val classification: String,
    @SerialName("issued_by") val issuedBy: String,
    val attributes: Map<String, String>,
    @SerialName("pii_attributes") val piiAttributes: List<String>
)