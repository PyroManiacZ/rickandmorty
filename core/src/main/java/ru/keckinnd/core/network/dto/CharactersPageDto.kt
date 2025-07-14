package ru.keckinnd.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharactersPageDto(
    val info: InfoDto,
    val results: List<CharacterDto>
)

@Serializable
data class InfoDto(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null
)
