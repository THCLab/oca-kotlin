package com.thehumancolossuslab.odca

import kotlinx.serialization.*

data class EncodeOverlay(
    private val encodeOverlayDto: EncodeOverlayDto
) { 
    val role = encodeOverlayDto.role
    val purpose = encodeOverlayDto.purpose
    val attrEncoding: MutableMap<String, String> = encodeOverlayDto.attrEncoding.toMutableMap()

    fun toDto(schemaBaseId: String, attributesUuid: MutableMap<String, String>): EncodeOverlayDto {
        return EncodeOverlayDto(
            context = encodeOverlayDto.context,
            type = encodeOverlayDto.type,
            description = encodeOverlayDto.description,
            issuedBy = encodeOverlayDto.issuedBy,
            role = role,
            purpose = purpose,
            schemaBaseId = schemaBaseId,
            attrEncoding = attrEncoding.mapKeys {
                attributesUuid[it.key] as String
            }
        )
    }

    fun add(attribute: AttributeDto) {
        if (attribute.encoding == null) { throw Exception() }
        
        attrEncoding.put(attribute.uuid, attribute.encoding)
    }

    fun modify(uuid: String, attribute: AttributeDto) {
        if (attribute.encoding == null) { throw Exception() }

        attrEncoding.set(uuid, attribute.encoding)
    }

    fun delete(uuid: String) {
        attrEncoding.remove(uuid)
    }
}