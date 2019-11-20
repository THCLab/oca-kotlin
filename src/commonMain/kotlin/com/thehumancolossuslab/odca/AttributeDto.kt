package com.thehumancolossuslab.odca

import kotlinx.serialization.*
import kotlin.random.*

data class AttributeDto(
    val name: String, val type: String, val isPii: Boolean,
    val label: String?, val format: String?
) {
    val uuid: String

    init {
        this.uuid = Random.nextLong(1, 999999999999999999).toString()
    }
}