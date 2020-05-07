package com.thehumancolossuslab.odca

import kotlinx.serialization.*

data class InformationOverlay(
    private val informationOverlayDto: InformationOverlayDto
) { 
    val role = informationOverlayDto.role
    val purpose = informationOverlayDto.purpose
    val language = informationOverlayDto.language
    val attrInformation: MutableMap<String, String> = informationOverlayDto.attrInformation.toMutableMap()

    fun toDto(schemaBaseId: String, attributesUuid: MutableMap<String, String>): InformationOverlayDto {
        return InformationOverlayDto(
            context = informationOverlayDto.context,
            type = informationOverlayDto.type,
            issuedBy = informationOverlayDto.issuedBy,
            role = role,
            purpose = purpose,
            schemaBaseId = schemaBaseId,
            language = language,
            attrInformation = attrInformation.mapKeys {
                attributesUuid[it.key] as String
            }
        )
    }

    fun add(attribute: AttributeDto, uuid: String = attribute.uuid) {
        val translation = attribute.translations?.get(language)
        if (translation?.get("information") == null) { throw Exception() }
        val information = translation["information"] as String

        attrInformation.put(uuid, information)
    }

    fun modify(uuid: String, attribute: AttributeDto) {
        val translation = attribute.translations?.get(language)
        if (translation?.get("information") == null) { throw Exception() }
        val information = translation["information"] as String

        attrInformation.set(uuid, information)
    }

    fun delete(uuid: String) {
        attrInformation.remove(uuid)
    }
}