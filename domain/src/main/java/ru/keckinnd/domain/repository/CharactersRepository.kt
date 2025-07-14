package ru.keckinnd.domain.repository

import ru.keckinnd.domain.model.Character

interface CharactersRepository {

    suspend fun getCharacters(
        page: Int = 1,
        query: String = "",
        filters: CharacterFilters = CharacterFilters()
    ): List<Character>

    suspend fun getCharacterById(id: Int): Character

    suspend fun getCharactersByIds(ids: List<Int>): List<Character>
}
