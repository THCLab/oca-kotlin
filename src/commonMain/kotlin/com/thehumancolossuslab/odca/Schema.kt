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
    val entryOverlays = schemaDto.entryOverlays.map { EntryOverlay(it.value) }.toMutableList()
    val characterEncodingOverlays = schemaDto.characterEncodingOverlays.map { CharacterEncodingOverlay(it.value) }.toMutableList()
    val informationOverlays = schemaDto.informationOverlays.map { InformationOverlay(it.value) }.toMutableList()

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
        val entryOverlayDtos: MutableMap<String, EntryOverlayDto> = mutableMapOf()
        entryOverlays.forEach {
            val value = it.toDto(schemaBaseLink, schemaBase.attributesUuid)
            val hashlink = HashlinkGenerator.call(
                Json.stringify(EntryOverlayDto.serializer(), value)
            )
            entryOverlayDtos.put("EntryOverlay-$hashlink", value)
        }
        val characterEncodingOverlayDtos: MutableMap<String, CharacterEncodingOverlayDto> = mutableMapOf()
        characterEncodingOverlays.forEach {
            val value = it.toDto(schemaBaseLink, schemaBase.attributesUuid)
            val hashlink = HashlinkGenerator.call(
                Json.stringify(CharacterEncodingOverlayDto.serializer(), value)
            )
            characterEncodingOverlayDtos.put("CharacterEncodingOverlay-$hashlink", value)
        }
        val informationOverlayDtos: MutableMap<String, InformationOverlayDto> = mutableMapOf()
        informationOverlays.forEach {
            val value = it.toDto(schemaBaseLink, schemaBase.attributesUuid)
            val hashlink = HashlinkGenerator.call(
                Json.stringify(InformationOverlayDto.serializer(), value)
            )
            informationOverlayDtos.put("InformationOverlay-$hashlink", value)
        }

        return SchemaDto(
            schemaBaseDto,
            labelOverlayDtos.toMap(),
            formatOverlayDtos.toMap(),
            entryOverlayDtos.toMap(),
            characterEncodingOverlayDtos.toMap(),
            informationOverlayDtos.toMap()
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
        if (entryOverlays.isNotEmpty()) {
            entryOverlays.forEach { overlay ->
                val tmp = overlay.attrEntries.mapKeys { entries ->
                    schemaBase.attributesUuid.filterValues { it == entries.key }.keys.first()
                }
                overlay.attrEntries.clear()
                tmp.forEach { overlay.attrEntries.put(it.key, it.value) }
            }
        }
        if (characterEncodingOverlays.isNotEmpty()) {
            characterEncodingOverlays.forEach { overlay ->
                val tmp = overlay.attrCharacterEncoding.mapKeys { entries ->
                    schemaBase.attributesUuid.filterValues { it == entries.key }.keys.first()
                }
                overlay.attrCharacterEncoding.clear()
                tmp.forEach { overlay.attrCharacterEncoding.put(it.key, it.value) }
            }
        }
        if (informationOverlays.isNotEmpty()) {
            informationOverlays.forEach { overlay ->
                val tmp = overlay.attrInformation.mapKeys { information ->
                    schemaBase.attributesUuid.filterValues { it == information.key }.keys.first()
                }
                overlay.attrInformation.clear()
                tmp.forEach { overlay.attrInformation.put(it.key, it.value) }
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

        attribute.translations?.forEach { translation ->
          val translationLang = translation.key

          if (translation.value["label"] != null) {
              var labelOverlay = labelOverlays.find { it.role == role && it.purpose == purpose && it.language == translationLang }
              if (labelOverlay == null) {
                  labelOverlay = LabelOverlay(
                      LabelOverlayDto(
                          role = role, purpose = purpose, language = translationLang
                      )
                  )
                  labelOverlays.add(labelOverlay)
              }

              labelOverlay.add(attribute)
          }

          if (translation.value["information"] != null) {
              var informationOverlay = informationOverlays.find { it.role == role && it.purpose == purpose && it.language == translationLang }
              if (informationOverlay == null) {
                  informationOverlay = InformationOverlay(
                      InformationOverlayDto(
                          role = role, purpose = purpose, language = translationLang
                      )
                  )
                  informationOverlays.add(informationOverlay)
              }
              informationOverlay.add(attribute)
          }

          if (translation.value["entry"] != null) {
              var entryOverlay = entryOverlays.find { it.role == role && it.purpose == purpose && it.language == translationLang}
              if (entryOverlay == null) {
                  entryOverlay = EntryOverlay(
                      EntryOverlayDto(
                          role = role, purpose = purpose, language = translationLang
                      )
                  )
                  entryOverlays.add(entryOverlay)
              }
              entryOverlay.add(attribute)
          }
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
        if (attribute.characterEncoding != null) {
            var characterEncodingOverlay = characterEncodingOverlays.find { it.role == role && it.purpose == purpose }
            if (characterEncodingOverlay == null) {
                characterEncodingOverlay = CharacterEncodingOverlay(
                    CharacterEncodingOverlayDto(
                        role = role, purpose = purpose
                    )
                )
                characterEncodingOverlays.add(characterEncodingOverlay)
            }
            characterEncodingOverlay.add(attribute)
        }
    }

    @JsName("modify")
    fun modify(uuid: String, attribute: AttributeDto) {
        schemaBase.modifyAttribute(uuid, attribute)

        val role = ""
        val purpose = ""

        attribute.translations?.forEach { translation ->
            val translationLang = translation.key

            if (translation.value["label"] != null) {
                var labelOverlay = labelOverlays.find {
                    it.role == role && it.purpose == purpose &&
                    it.language == translationLang
                }
                if (labelOverlay == null) {
                    labelOverlay = LabelOverlay(
                        LabelOverlayDto(
                            role = role, purpose = purpose,
                            language = translationLang
                        )
                    )
                    labelOverlays.add(labelOverlay)
                }
                labelOverlay.modify(uuid, attribute)
            }

            if (translation.value["information"] != null) {
                var informationOverlay = informationOverlays.find {
                    it.role == role && it.purpose == purpose &&
                    it.language == translationLang
                }
                if (informationOverlay == null) {
                    informationOverlay = InformationOverlay(
                        InformationOverlayDto(
                            role = role, purpose = purpose,
                            language = translationLang
                        )
                    )
                    informationOverlays.add(informationOverlay)
                }

                if (informationOverlay.attrInformation.containsKey(uuid)) {
                    informationOverlay.modify(uuid, attribute)
                } else {
                    informationOverlay.add(attribute, uuid)
                }
            }

            if (translation.value["entry"] != null) {
                var entryOverlay = entryOverlays.find {
                  it.role == role && it.purpose == purpose &&
                  it.language == translationLang
                }
                if (entryOverlay == null) {
                    entryOverlay = EntryOverlay(
                        EntryOverlayDto(
                            role = role, purpose = purpose,
                            language = translationLang
                        )
                    )
                    entryOverlays.add(entryOverlay)
                }

                if (entryOverlay.attrEntries.containsKey(uuid)) {
                    entryOverlay.modify(uuid, attribute)
                } else {
                    entryOverlay.add(attribute, uuid)
                }
            }
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

        if (attribute.characterEncoding != null) {
            var characterEncodingOverlay = characterEncodingOverlays.find { it.role == role && it.purpose == purpose }
            if (characterEncodingOverlay == null) {
                characterEncodingOverlay = CharacterEncodingOverlay(
                    CharacterEncodingOverlayDto(
                        role = role, purpose = purpose
                    )
                )
                characterEncodingOverlays.add(characterEncodingOverlay)
            }
            characterEncodingOverlay.modify(uuid, attribute)
        }
    }

    @JsName("delete")
    fun delete(uuid: String) {
        schemaBase.deleteAttribute(uuid)

        val role = ""
        val purpose = ""

        labelOverlays.forEach { overlay -> overlay.delete(uuid) }

        informationOverlays.forEach { overlay -> overlay.delete(uuid) }

        entryOverlays.forEach { overlay -> overlay.delete(uuid) }

        var formatOverlay = formatOverlays.find { it.role == role && it.purpose == purpose }
        formatOverlay?.delete(uuid)

        var characterEncodingOverlay = characterEncodingOverlays.find { it.role == role && it.purpose == purpose }
        characterEncodingOverlay?.delete(uuid)
    }
}