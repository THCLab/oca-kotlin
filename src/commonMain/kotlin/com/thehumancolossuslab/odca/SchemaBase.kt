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

    fun addAttribute(attribute: AttributeDto) {
        attributesUuid.put(attribute.uuid, attribute.name)
        attributesType.put(attribute.uuid, attribute.type)
        if (attribute.isPii == true) {
            piiAttributes.add(attribute.uuid)
        }
    }

    fun modifyAttribute(uuid: String, attribute: AttributeDto) {
        if (attribute.name.isBlank()) {
            throw Exception("Attribute name cannot be empty")
        }
        val uuidsByAttrName = attributesUuid.filterValues { it == attribute.name }.keys
        if (!uuidsByAttrName.isEmpty() && !uuidsByAttrName.contains(uuid)) {
            throw Exception("Attribute name '${attribute.name}' is already set")
        }

        attributesUuid.put(uuid, attribute.name)
        attributesType.put(uuid, attribute.type)
        if (attribute.isPii == false) {
            piiAttributes.remove(uuid)
        } else if (!piiAttributes.contains(uuid)) {
            piiAttributes.add(uuid)
        }
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