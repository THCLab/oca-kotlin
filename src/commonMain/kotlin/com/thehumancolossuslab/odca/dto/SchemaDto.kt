package com.thehumancolossuslab.odca

import kotlinx.serialization.*

@Serializable
class SchemaDto(
    val schemaBase: SchemaBaseDto,
    val labelOverlays: Map<String, LabelOverlayDto> = mapOf(),
    val formatOverlays: Map<String, FormatOverlayDto> = mapOf(),
    val entryOverlays: Map<String, EntryOverlayDto> = mapOf(),
    val encodeOverlays: Map<String, EncodeOverlayDto> = mapOf(),
    val informationOverlays: Map<String, InformationOverlayDto> = mapOf()
)