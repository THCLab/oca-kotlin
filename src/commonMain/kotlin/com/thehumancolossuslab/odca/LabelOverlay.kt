package com.thehumancolossuslab.odca

import kotlinx.serialization.*

@Serializable
data class LabelOverlay(
    @SerialName("@context") val context: String = "https://odca.tech/overlays/v1",
    val type: String = "spec/overlay/label/1.0",
    val description: String = "",
    @SerialName("issued_by") val issuedBy: String = "",
    val role: String,
    val purpose: String,
    @SerialName("schema_base") var schemaBaseId: String,
    val language: String,
    @SerialName("attr_labels") val attrLabels: MutableMap<String, String> = mutableMapOf(),
    @SerialName("attr_categories") val attrCategories: MutableList<String> = mutableListOf(),
    @SerialName("category_labels") val categoryLabels: MutableMap<String, String> = mutableMapOf(),
    @SerialName("category_attributes") val categoryAttributes: MutableMap<String, MutableList<String>> = mutableMapOf()
) { 
    fun add(uuid: String, input: String) {
        var (category, label) = splitInput(input)
        
        attrLabels.put(uuid, label)
        if (category.isNotBlank()) {
            var categoryAttr = snakeCase(category)
            attrCategories.add(categoryAttr)
            categoryLabels.put(categoryAttr, category)
            categoryAttributes.getOrPut(categoryAttr) { mutableListOf() }.add(uuid)
        }
    }

    fun modify(uuid: String, input: String) {
        delete(uuid)
        add(uuid, input)
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