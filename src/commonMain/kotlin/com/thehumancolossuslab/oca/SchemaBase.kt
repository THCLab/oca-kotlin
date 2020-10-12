package com.thehumancolossuslab.oca

data class SchemaBase(
    val schemaBaseDto: SchemaBaseDto
) {
    val context: String = schemaBaseDto.context
    val name: String = schemaBaseDto.name
    val type: String = schemaBaseDto.type
    val description: String = schemaBaseDto.description
    val classification: String = schemaBaseDto.classification
    val issuedBy: String = schemaBaseDto.issuedBy
    var attributesType: MutableMap<String, String> = schemaBaseDto.attributes.toMutableMap()
    var attributesUuid: MutableMap<String, String> = mutableMapOf()
    var piiAttributes: MutableList<String> = schemaBaseDto.piiAttributes.toMutableList()

    fun toDto(): SchemaBaseDto {
        val attributes: MutableMap<String, String> = mutableMapOf()
        attributesType.forEach {
            val attrName = attributesUuid.get(it.key)
            if (attrName.isNullOrBlank()) {
                throw Exception("Attribute name for ${it.key} is ${attrName}")
            }
            attributes.put(attrName, it.value)
        }

        return SchemaBaseDto(
            context = schemaBaseDto.context,
            name = schemaBaseDto.name,
            type = schemaBaseDto.type,
            description = schemaBaseDto.description,
            classification = schemaBaseDto.classification,
            issuedBy = schemaBaseDto.issuedBy,
            attributes = attributes.toMap(),
            piiAttributes = piiAttributes.map {
                attributesUuid.get(it) as String
            }
        )
    }

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