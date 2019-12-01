package com.thehumancolossuslab.odca

import kotlinx.serialization.*

data class FormatOverlay(
    private val formatOverlayDto: FormatOverlayDto
) {
    val role = formatOverlayDto.role
    val purpose = formatOverlayDto.purpose
    val attrFormats: MutableMap<String, String> = formatOverlayDto.attrFormats.toMutableMap()

    fun toDto(schemaBaseId: String, attributesUuid: MutableMap<String, String>): FormatOverlayDto {
        return FormatOverlayDto(
            context = formatOverlayDto.context,
            type = formatOverlayDto.type,
            issuedBy = formatOverlayDto.issuedBy,
            role = role,
            purpose = purpose,
            schemaBaseId = schemaBaseId,
            attrFormats = attrFormats.mapKeys {
                attributesUuid[it.key] as String
            }
        )
    }

    fun add(attribute: AttributeDto) {
        if (attribute.format == null) { throw Exception() }
        attrFormats.put(attribute.uuid, attribute.format)
    }

    fun modify(uuid: String, attribute: AttributeDto) {
        if (attribute.format == null) { throw Exception() }
        attrFormats.set(uuid, attribute.format)
    }

    fun delete(uuid: String) {
        attrFormats.remove(uuid)
    }
}