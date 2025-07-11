package ru.keckinnd.core.network

import ru.keckinnd.core.network.dto.CharacterDto
import ru.keckinnd.core.network.dto.CharactersPageDto


interface RickAndMortyApi {
    suspend fun getCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null
    ): CharactersPageDto

    suspend fun getCharacterById(id: Int): CharacterDto

    suspend fun getCharactersByIds(ids: List<Int>): List<CharacterDto>
}
