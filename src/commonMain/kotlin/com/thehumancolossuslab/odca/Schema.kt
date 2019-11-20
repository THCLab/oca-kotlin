package com.thehumancolossuslab.odca

import kotlin.js.*
import kotlin.random.*
import kotlinx.serialization.*

class Schema(
    val uuid: String = Random.nextLong(1, 999999999999999999).toString(),
    val schemaBase: SchemaBase,
    val labelOverlays: List<LabelOverlay>,
    val formatOverlays: List<FormatOverlay>
) {
    init {
        schemaBase.attributesType.forEach {
            val uuid = Random.nextLong(1, 999999999999999999).toString()
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

        schemaBase.addAttribute(
            attribute.uuid, attribute.name, attribute.type, attribute.isPii
        )
        if (attribute.label != null) {
            var labelOverlay = labelOverlays.find { it.role == "" && it.purpose == "" }
            labelOverlay?.add(attribute.uuid, attribute.label)
        }
        
        if (attribute.format != null) {
            var formatOverlay = formatOverlays.find { it.role == "" && it.purpose == "" }
            formatOverlay?.add(attribute.uuid, attribute.format)
        }
    }

    @JsName("modify")
    fun modify(uuid: String, attribute: AttributeDto) {
        schemaBase.modifyAttribute(
            uuid, attribute.name, attribute.type, attribute.isPii
        )

        if (attribute.label != null) {
            var labelOverlay = labelOverlays.find { it.role == "" && it.purpose == "" }
            labelOverlay?.modify(uuid, attribute.label)
        }

        if (attribute.format != null) {
            var formatOverlay = formatOverlays.find { it.role == "" && it.purpose == "" }
            formatOverlay?.modify(uuid, attribute.format)
        }
    }

    @JsName("delete")
    fun delete(uuid: String) {
        schemaBase.deleteAttribute(uuid)

        var labelOverlay = labelOverlays.find { it.role == "" && it.purpose == "" }
        labelOverlay?.delete(uuid)

        var formatOverlay = formatOverlays.find { it.role == "" && it.purpose == "" }
        formatOverlay?.delete(uuid)
    }
}
