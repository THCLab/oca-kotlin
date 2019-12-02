package com.thehumancolossuslab.odca

import com.benasher44.uuid.uuid4
import kotlinx.serialization.*

data class AttributeDto(
    val name: String, val type: String, val isPii: Boolean,
    val categories: Array<String>?, val label: String?, val format: String?,
    val entries: Array<String>?, val encoding: String?, val information: String?
) {
    val uuid: String

    init {
        this.uuid = uuid4().toString()
    }
}