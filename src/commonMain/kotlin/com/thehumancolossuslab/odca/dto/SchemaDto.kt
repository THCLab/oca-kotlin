package com.thehumancolossuslab.odca

import kotlinx.serialization.*

@Serializable
class SchemaDto(
    val schemaBase: SchemaBaseDto,
    val labelOverlays: Map<String, LabelOverlayDto>,
    val formatOverlays: Map<String, FormatOverlayDto>,
    val entryOverlays: Map<String, EntryOverlayDto>,
    val encodeOverlays: Map<String, EncodeOverlayDto>
)
