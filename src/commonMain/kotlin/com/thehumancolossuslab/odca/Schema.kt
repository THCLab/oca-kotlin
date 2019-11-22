package com.thehumancolossuslab.odca

import com.benasher44.uuid.uuid4
import kotlin.js.*
import kotlinx.serialization.*

class Schema(
    val uuid: String = uuid4().toString(),
    val schemaBase: SchemaBase,
    val labelOverlays: MutableList<LabelOverlay>,
    val formatOverlays: MutableList<FormatOverlay>
) {
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
            throw Exception("Attribute uuid, type are required: ${attribute}")
        }

        schemaBase.addAttribute(attribute)
        val schemaBaseLink = HashlinkGenerator.call(schemaBase)
        labelOverlays.forEach { overlay ->
            overlay.schemaBaseId = schemaBaseLink
        }
        formatOverlays.forEach { overlay ->
            overlay.schemaBaseId = schemaBaseLink
        }

        val role = ""
        val purpose = ""

        if (attribute.label != null) {
            var labelOverlay = labelOverlays.find { it.role == role && it.purpose == purpose }
            if (labelOverlay == null) {
                labelOverlay = LabelOverlay(
                    role = role, purpose = purpose, language = "en_US", schemaBaseId = schemaBaseLink
                )
                labelOverlays.add(labelOverlay)
            }

            labelOverlay.add(attribute)
        }
        
        if (attribute.format != null) {
            var formatOverlay = formatOverlays.find { it.role == role && it.purpose == purpose }
            if (formatOverlay == null) {
                formatOverlay = FormatOverlay(role = role, purpose = purpose, schemaBaseId = schemaBaseLink)
                formatOverlays.add(formatOverlay)
            }
            formatOverlay.add(attribute)
        }
    }

    @JsName("modify")
    fun modify(uuid: String, attribute: AttributeDto) {
        schemaBase.modifyAttribute(uuid, attribute)

        val schemaBaseLink = HashlinkGenerator.call(schemaBase)
        labelOverlays.forEach { overlay ->
            overlay.schemaBaseId = schemaBaseLink
        }
        formatOverlays.forEach { overlay ->
            overlay.schemaBaseId = schemaBaseLink
        }

        val role = ""
        val purpose = ""

        if (attribute.label != null) {
            var labelOverlay = labelOverlays.find { it.role == role && it.purpose == purpose }
            if (labelOverlay == null) {
                labelOverlay = LabelOverlay(
                    role = role, purpose = purpose, language = "en_US", schemaBaseId = schemaBaseLink
                )
                labelOverlays.add(labelOverlay)
            }
            labelOverlay.modify(uuid, attribute)
        }

        if (attribute.format != null) {
            var formatOverlay = formatOverlays.find { it.role == role && it.purpose == purpose }
            if (formatOverlay == null) {
                formatOverlay = FormatOverlay(role = role, purpose = purpose, schemaBaseId = schemaBaseLink)
                formatOverlays.add(formatOverlay)
            }
            formatOverlay.modify(uuid, attribute)
        }
    }

    @JsName("delete")
    fun delete(uuid: String) {
        schemaBase.deleteAttribute(uuid)

        val schemaBaseLink = HashlinkGenerator.call(schemaBase)
        labelOverlays.forEach { overlay ->
            overlay.schemaBaseId = schemaBaseLink
        }
        formatOverlays.forEach { overlay ->
            overlay.schemaBaseId = schemaBaseLink
        }

        val role = ""
        val purpose = ""

        var labelOverlay = labelOverlays.find { it.role == role && it.purpose == purpose }
        labelOverlay?.delete(uuid)

        var formatOverlay = formatOverlays.find { it.role == role && it.purpose == purpose }
        formatOverlay?.delete(uuid)
    }
}
