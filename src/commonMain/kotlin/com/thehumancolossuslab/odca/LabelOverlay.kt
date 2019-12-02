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
        if (attribute.label == null || attribute.categories == null) {
            throw Exception()
        }
        val label = attribute.label
        val categories = attribute.categories
        
        attrLabels.put(uuid, label)

        val category = categories.last()
        val supercategories = categories.dropLast(1)

        var supercategoryNumbers : MutableList<String> = mutableListOf()
        for (supercategory in supercategories) {
            val supercategoryAttr = getOrCreateCategoryAttr(supercategoryNumbers, supercategory)
            supercategoryNumbers.add(supercategoryAttr.dropLast(1).last().toString())
            if (supercategoryAttr !in attrCategories) {
                attrCategories.add(supercategoryAttr)
            }
            categoryLabels.put(supercategoryAttr, supercategory)
        }

        var categoryAttr = getOrCreateCategoryAttr(supercategoryNumbers, category)
        if (categoryAttr !in attrCategories) {
            attrCategories.add(categoryAttr)
        }
        categoryLabels.put(categoryAttr, category)
        categoryAttributes.getOrPut(categoryAttr) { mutableListOf() }.add(uuid)
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

    private fun getOrCreateCategoryAttr(supercategoryNumbers: List<String>, category: String) : String {
        val nestedCategoryNumbers = if (supercategoryNumbers.size > 0)
            ("_" + supercategoryNumbers.joinToString("_") + "_") else "_"

        var categoryAttr = categoryLabels.filter {
            it.value == category
            && it.key.matches(Regex("_category$nestedCategoryNumbers[1-9]_"))
        }.keys

        if (categoryAttr.size > 0) {
            return categoryAttr.first()
        } else {
            val subcategoryNumber = categoryLabels.count {
                it.key.matches(Regex("_category$nestedCategoryNumbers[1-9]_"))
            } + 1
            return "_category${nestedCategoryNumbers}${subcategoryNumber}_"
        }
    }
}