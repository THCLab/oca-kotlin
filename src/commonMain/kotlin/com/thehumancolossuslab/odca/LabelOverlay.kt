package com.thehumancolossuslab.odca

import kotlinx.serialization.*

data class LabelOverlay(
    private val labelOverlayDto: LabelOverlayDto
) { 
    val role = labelOverlayDto.role
    val purpose = labelOverlayDto.purpose
    val attrLabels: MutableMap<String, String> = labelOverlayDto.attrLabels.toMutableMap()
    val attrCategories: MutableList<String> = labelOverlayDto.attrCategories.toMutableList()
    val categoryLabels: MutableMap<String, String> = labelOverlayDto.categoryLabels.toMutableMap()
    val categoryAttributes: MutableMap<String, MutableList<String>> = labelOverlayDto.categoryAttributes.toMutableMap()

    fun toDto(schemaBaseId: String, attributesUuid: MutableMap<String, String>): LabelOverlayDto {
        val categoryAttrs: MutableMap<String, MutableList<String>> = mutableMapOf()
        categoryAttributes.forEach {
            val value = it.value.map { attributesUuid.get(it) as String }
            categoryAttrs.put(it.key, value.toMutableList())
        }

        return LabelOverlayDto(
            context = labelOverlayDto.context,
            type = labelOverlayDto.type,
            description = labelOverlayDto.description,
            issuedBy = labelOverlayDto.issuedBy,
            role = role,
            purpose = purpose,
            schemaBaseId = schemaBaseId,
            language = labelOverlayDto.language,
            attrLabels = attrLabels.mapKeys {
                attributesUuid[it.key] as String
            },
            attrCategories = attrCategories,
            categoryLabels = categoryLabels,
            categoryAttributes = categoryAttrs
        )
    }

    fun add(attribute: AttributeDto, uuid: String = attribute.uuid) {
        if (attribute.label == null) { throw Exception() }
        var (category, label) = splitInput(attribute.label)
        
        attrLabels.put(uuid, label)
        if (category.isNotBlank()) {
            var categoryAttr = snakeCase(category)
            attrCategories.add(categoryAttr)
            categoryLabels.put(categoryAttr, category)
            categoryAttributes.getOrPut(categoryAttr) { mutableListOf() }.add(uuid)
        }
    }

    fun modify(uuid: String, attribute: AttributeDto) {
        delete(uuid)
        add(attribute, uuid)
    }

    fun delete(uuid: String) {
        attrLabels.remove(uuid)

        var categoryAttr: String = ""
        categoryAttributes.forEach { 
            if (it.value.contains(uuid)) {
                categoryAttr = it.key
            }
        }
        if (!categoryAttr.isNullOrBlank()) {
            categoryAttributes.get(categoryAttr)!!.remove(uuid)
            if (categoryAttributes.get(categoryAttr)!!.isEmpty()) {
                categoryAttributes.remove(categoryAttr)
                categoryLabels.remove(categoryAttr)
                attrCategories.remove(categoryAttr)
            }
        }
    }

    private fun splitInput(input: String) : List<String> {
        var result = input.split("|").map { it.trim() }
        if (result.size == 1) {
            result = listOf("") + result
        }
        return result
    }

    private fun snakeCase(input: String) : String {
        return input.toLowerCase().replace(" ", "_")
    }
}