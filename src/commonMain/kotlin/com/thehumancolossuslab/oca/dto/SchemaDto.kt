package com.thehumancolossuslab.oca

import kotlinx.serialization.*

@Serializable
class SchemaDto(
    val schemaBase: SchemaBaseDto,
    val labelOverlays: Map<String, LabelOverlayDto> = mapOf(),
    val formatOverlays: Map<String, FormatOverlayDto> = mapOf(),
    val entryOverlays: Map<String, EntryOverlayDto> = mapOf(),
    val characterEncodingOverlays: Map<String, CharacterEncodingOverlayDto> = mapOf(),
    val informationOverlays: Map<String, InformationOverlayDto> = mapOf()
)