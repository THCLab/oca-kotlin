package com.thehumancolossuslab.odca

import kotlinx.serialization.*

data class EntryOverlay(
    private val entryOverlayDto: EntryOverlayDto
) { 
    val role = entryOverlayDto.role
    val purpose = entryOverlayDto.purpose
    val attrEntries: MutableMap<String, MutableList<String>> = entryOverlayDto.attrEntries.toMutableMap()

    fun toDto(schemaBaseId: String, attributesUuid: MutableMap<String, String>): EntryOverlayDto {
        return EntryOverlayDto(
            context = entryOverlayDto.context,
            type = entryOverlayDto.type,
            issuedBy = entryOverlayDto.issuedBy,
            role = role,
            purpose = purpose,
            schemaBaseId = schemaBaseId,
            language = entryOverlayDto.language,
            attrEntries = attrEntries.mapKeys {
                attributesUuid[it.key] as String
            }
        )
    }

    fun add(attribute: AttributeDto, uuid: String = attribute.uuid) {
        if (attribute.entries == null) { throw Exception() }
        
        attrEntries.put(uuid, attribute.entries.toMutableList())
    }

    fun modify(uuid: String, attribute: AttributeDto) {
        if (attribute.entries == null) { throw Exception() }

        attrEntries.set(uuid, attribute.entries.toMutableList())
    }

    fun delete(uuid: String) {
        attrEntries.remove(uuid)
    }
}