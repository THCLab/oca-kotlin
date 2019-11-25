package com.thehumancolossuslab.odca

import kotlinx.serialization.*

data class InformationOverlay(
    private val informationOverlayDto: InformationOverlayDto
) { 
    val role = informationOverlayDto.role
    val purpose = informationOverlayDto.purpose
    val attrInformation: MutableMap<String, String> = informationOverlayDto.attrInformation.toMutableMap()

    fun toDto(schemaBaseId: String, attributesUuid: MutableMap<String, String>): InformationOverlayDto {
        return InformationOverlayDto(
            context = informationOverlayDto.context,
            type = informationOverlayDto.type,
            description = informationOverlayDto.description,
            issuedBy = informationOverlayDto.issuedBy,
            role = role,
            purpose = purpose,
            schemaBaseId = schemaBaseId,
            language = informationOverlayDto.language,
            attrInformation = attrInformation.mapKeys {
                attributesUuid[it.key] as String
            }
        )
    }

    fun add(attribute: AttributeDto, uuid: String = attribute.uuid) {
        if (attribute.information == null) { throw Exception() }
        
        attrInformation.put(uuid, attribute.information)
    }

    fun modify(uuid: String, attribute: AttributeDto) {
        if (attribute.information == null) { throw Exception() }

        attrInformation.set(uuid, attribute.information)
    }

    fun delete(uuid: String) {
        attrInformation.remove(uuid)
    }
}