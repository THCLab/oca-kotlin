package com.thehumancolossuslab.oca

import kotlinx.serialization.UnstableDefault

@UnstableDefault
@ExperimentalUnsignedTypes
class SchemaRenderer(val baseInfo: Map<String, String>, val attributes: Array<AttributeDto>) {
    fun call(): Schema {
        val schema = Schema(
            SchemaDto(
                schemaBase = SchemaBaseDto(
                    name = baseInfo.getValue("name"),
                    description = baseInfo.getValue("description"),
                    classification = baseInfo.getValue("classification")
                )
            )
        )

        attributes.forEach { attribute ->
            schema.add(attribute)
        }

        return schema
    }
}
