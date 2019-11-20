package com.thehumancolossuslab.odca

import kotlinx.serialization.*

@Serializable
data class SchemaBase(
    @SerialName("@context") val context: String,
    val name: String,
    val type: String,
    val description: String,
    val classification: String,
    @SerialName("issued_by") val issuedBy: String,
    @SerialName("attributes") val attributesType: MutableMap<String, String>,
    val attributesUuid: MutableMap<String, String> = mutableMapOf(),
    @SerialName("pii_attributes") val piiAttributes: MutableList<String>
) {

    fun addAttribute(
        uuid: String, attrName: String, attrType: String, isPii: Boolean?
    ) {
        attributesUuid.put(uuid, attrName)
        attributesType.put(uuid, attrType)
        if (isPii == true) {
            piiAttributes.add(uuid)
        }
    }

    fun modifyAttribute(
        uuid: String, attrName: String, attrType: String, isPii: Boolean
    ) {
        if (attrName.isBlank()) {
            throw Exception("Attribute name cannot be empty")
        }
        val uuidsByAttrName = attributesUuid.filterValues { it == attrName }.keys
        if (!uuidsByAttrName.isEmpty() && !uuidsByAttrName.contains(uuid)) {
            throw Exception("Attribute name '$attrName' is already set")
        }

        deleteAttribute(uuid)
        addAttribute(uuid, attrName, attrType, isPii)
    }

    fun deleteAttribute(uuid: String) {
        if (!attributesUuid.contains(uuid)) {
            throw Exception("Attribute does not exists: ${uuid}")
        }

        attributesUuid.remove(uuid)
        attributesType.remove(uuid)
        piiAttributes.remove(uuid)
    }
}