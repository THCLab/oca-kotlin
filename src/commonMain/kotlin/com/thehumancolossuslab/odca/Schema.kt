package com.thehumancolossuslab.odca

import com.benasher44.uuid.uuid4
import kotlin.js.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class Schema(
    private val schemaDto: SchemaDto,
    val uuid: String = uuid4().toString()
) {
    val schemaBase = SchemaBase(schemaDto.schemaBase)

    val labelOverlays = schemaDto.labelOverlays.map { LabelOverlay(it.value) }.toMutableList()
    val formatOverlays = schemaDto.formatOverlays.map { FormatOverlay(it.value) }.toMutableList()

    @UnstableDefault
    @ExperimentalUnsignedTypes
    fun toDto(): SchemaDto {
        val schemaBaseDto = schemaBase.toDto()
        val schemaBaseLink = HashlinkGenerator.call(
            Json.stringify(SchemaBaseDto.serializer(), schemaBaseDto)
        )
        val labelOverlayDtos: MutableMap<String, LabelOverlayDto> = mutableMapOf()
        labelOverlays.forEach {
            val value = it.toDto(schemaBaseLink, schemaBase.attributesUuid)
            val hashlink = HashlinkGenerator.call(
                Json.stringify(LabelOverlayDto.serializer(), value)
            )
            labelOverlayDtos.put("LabelOverlay-$hashlink", value)
        }
        val formatOverlayDtos: MutableMap<String, FormatOverlayDto> = mutableMapOf()
        formatOverlays.forEach {
            val value = it.toDto(schemaBaseLink, schemaBase.attributesUuid)
            val hashlink = HashlinkGenerator.call(
                Json.stringify(FormatOverlayDto.serializer(), value)
            )
            formatOverlayDtos.put("FormatOverlay-$hashlink", value)
        }

        return SchemaDto(
            schemaBaseDto,
            labelOverlayDtos.toMap(),
            formatOverlayDtos.toMap()
        )
    }

    init {
        schemaBase.attributesType.forEach {
            val uuid = uuid4().toString()
            schemaBase.attributesUuid.put(uuid, it.key)
        }
        val tmpMap = schemaBase.attributesType.mapKeys { attr ->
            schemaBase.attributesUuid.filterValues { it == attr.key }.keys.first()
        }
        schemaBase.attributesType.clear()
        tmpMap.forEach { schemaBase.attributesType.put(it.key, it.value) }

        val tmpList = schemaBase.piiAttributes.map { attr ->
            schemaBase.attributesUuid.filterValues { it == attr }.keys.first()
        }
        schemaBase.piiAttributes.clear()
        tmpList.forEach { schemaBase.piiAttributes.add(it) }

        if (labelOverlays.isNotEmpty()) {
            labelOverlays.forEach { overlay ->
                overlay.categoryAttributes.forEach {
                    val tmpCat = it.value.map { attr ->
                        schemaBase.attributesUuid.filterValues { it == attr }.keys.first()
                    }
                    it.value.clear()
                    tmpCat.forEach { tmp ->
                        it.value.add(tmp)
                    }
                }
                val tmpLab = overlay.attrLabels.mapKeys { label ->
                    schemaBase.attributesUuid.filterValues { it == label.key }.keys.first()
                }
                overlay.attrLabels.clear()
                tmpLab.forEach { overlay.attrLabels.put(it.key, it.value) }
            }
        }
        if (formatOverlays.isNotEmpty()) {
            formatOverlays.forEach { overlay ->
                val tmp = overlay.attrFormats.mapKeys { format ->
                    schemaBase.attributesUuid.filterValues { it == format.key }.keys.first()
                }
                overlay.attrFormats.clear()
                tmp.forEach { overlay.attrFormats.put(it.key, it.value) }
            }
        }
    }

    @JsName("add")
    fun add(attribute: AttributeDto) {
        if (attribute.uuid.isNullOrEmpty() ||
            attribute.type.isNullOrEmpty()) {
            throw Exception("Attribute uuid, type are required: $attribute")
        }

        schemaBase.addAttribute(attribute)

        val role = ""
        val purpose = ""

        if (attribute.label != null) {
            var labelOverlay = labelOverlays.find { it.role == role && it.purpose == purpose }
            if (labelOverlay == null) {
                labelOverlay = LabelOverlay(
                    LabelOverlayDto(
                        role = role, purpose = purpose, language = "en_US"
                    )
                )
                labelOverlays.add(labelOverlay)
            }

            labelOverlay.add(attribute)
        }

        if (attribute.format != null) {
            var formatOverlay = formatOverlays.find { it.role == role && it.purpose == purpose }
            if (formatOverlay == null) {
                formatOverlay = FormatOverlay(
                    FormatOverlayDto(
                        role = role, purpose = purpose
                    )
                )
                formatOverlays.add(formatOverlay)
            }
            formatOverlay.add(attribute)
        }
    }

    @JsName("modify")
    fun modify(uuid: String, attribute: AttributeDto) {
        schemaBase.modifyAttribute(uuid, attribute)

        val role = ""
        val purpose = ""

        if (attribute.label != null) {
            var labelOverlay = labelOverlays.find { it.role == role && it.purpose == purpose }
            if (labelOverlay == null) {
                labelOverlay = LabelOverlay(
                    LabelOverlayDto(
                        role = role, purpose = purpose, language = "en_US"
                    )
                )
                labelOverlays.add(labelOverlay)
            }
            labelOverlay.modify(uuid, attribute)
        }

        if (attribute.format != null) {
            var formatOverlay = formatOverlays.find { it.role == role && it.purpose == purpose }
            if (formatOverlay == null) {
                formatOverlay = FormatOverlay(
                    FormatOverlayDto(
                        role = role, purpose = purpose
                    )
                )
                formatOverlays.add(formatOverlay)
            }
            formatOverlay.modify(uuid, attribute)
        }
    }

    @JsName("delete")
    fun delete(uuid: String) {
        schemaBase.deleteAttribute(uuid)

        val role = ""
        val purpose = ""

        var labelOverlay = labelOverlays.find { it.role == role && it.purpose == purpose }
        labelOverlay?.delete(uuid)

        var formatOverlay = formatOverlays.find { it.role == role && it.purpose == purpose }
        formatOverlay?.delete(uuid)
    }
}
