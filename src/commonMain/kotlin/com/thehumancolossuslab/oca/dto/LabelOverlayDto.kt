package com.thehumancolossuslab.oca

import kotlinx.serialization.*

@Serializable
data class LabelOverlayDto(
    @SerialName("@context") val context: String = "https://odca.tech/overlays/v1",
    val type: String = "spec/overlay/label/1.0",
    @SerialName("issued_by") val issuedBy: String = "",
    val role: String,
    val purpose: String,
    @SerialName("schema_base") var schemaBaseId: String = "",
    val language: String,
    @SerialName("attr_labels") val attrLabels: Map<String, String> = mapOf(),
    @SerialName("attr_categories") val attrCategories: List<String> = listOf(),
    @SerialName("cat_labels") val categoryLabels: Map<String, String> = mapOf(),
    @SerialName("cat_attributes") val categoryAttributes: Map<String, MutableList<String>> = mapOf()
)