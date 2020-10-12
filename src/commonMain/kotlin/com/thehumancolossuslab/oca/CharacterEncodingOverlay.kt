package com.thehumancolossuslab.oca

data class CharacterEncodingOverlay(
    private val characterEncodingOverlayDto: CharacterEncodingOverlayDto
) { 
    val role = characterEncodingOverlayDto.role
    val purpose = characterEncodingOverlayDto.purpose
    val attrCharacterEncoding: MutableMap<String, String> = characterEncodingOverlayDto.attrCharacterEncoding.toMutableMap()

    fun toDto(schemaBaseId: String, attributesUuid: MutableMap<String, String>): CharacterEncodingOverlayDto {
        return CharacterEncodingOverlayDto(
            context = characterEncodingOverlayDto.context,
            type = characterEncodingOverlayDto.type,
            issuedBy = characterEncodingOverlayDto.issuedBy,
            role = role,
            purpose = purpose,
            schemaBaseId = schemaBaseId,
            attrCharacterEncoding = attrCharacterEncoding.mapKeys {
                attributesUuid[it.key] as String
            }
        )
    }

    fun add(attribute: AttributeDto) {
        if (attribute.characterEncoding == null) { throw Exception() }
        
        attrCharacterEncoding.put(attribute.uuid, attribute.characterEncoding)
    }

    fun modify(uuid: String, attribute: AttributeDto) {
        if (attribute.characterEncoding == null) { throw Exception() }

        attrCharacterEncoding.set(uuid, attribute.characterEncoding)
    }

    fun delete(uuid: String) {
        attrCharacterEncoding.remove(uuid)
    }
}