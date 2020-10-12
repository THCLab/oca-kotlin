package com.thehumancolossuslab.oca

data class EntryOverlay(
    private val entryOverlayDto: EntryOverlayDto
) { 
    val role = entryOverlayDto.role
    val purpose = entryOverlayDto.purpose
    val language = entryOverlayDto.language
    val attrEntries: MutableMap<String, MutableList<String>> = entryOverlayDto.attrEntries.toMutableMap()

    fun toDto(schemaBaseId: String, attributesUuid: MutableMap<String, String>): EntryOverlayDto {
        return EntryOverlayDto(
            context = entryOverlayDto.context,
            type = entryOverlayDto.type,
            issuedBy = entryOverlayDto.issuedBy,
            role = role,
            purpose = purpose,
            schemaBaseId = schemaBaseId,
            language = language,
            attrEntries = attrEntries.mapKeys {
                attributesUuid[it.key] as String
            }
        )
    }

    fun add(attribute: AttributeDto, uuid: String = attribute.uuid) {
        val translation = attribute.translations?.get(language)
        if (translation?.get("entry") == null) { throw Exception() }
        val entries = translation["entry"] as Array<String>

        attrEntries.put(uuid, entries?.toMutableList())
    }

    fun modify(uuid: String, attribute: AttributeDto) {
        val translation = attribute.translations?.get(language)
        if (translation?.get("entry") == null) { throw Exception() }
        val entries = translation["entry"] as Array<String>

        attrEntries.set(uuid, entries.toMutableList())
    }

    fun delete(uuid: String) {
        attrEntries.remove(uuid)
    }
}