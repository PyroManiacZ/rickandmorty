package ru.keckinnd.data.source

import ru.keckinnd.core.network.RickAndMortyApi
import ru.keckinnd.core.network.dto.CharacterDto
import javax.inject.Inject

class CharactersRemoteDataSource @Inject constructor(
    private val api: RickAndMortyApi
) {
    suspend fun getCharacters(
        page: Int,
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ) = api.getCharacters(page, name, status, species, type, gender)

    suspend fun getCharacterById(id: Int): CharacterDto {
        return api.getCharacterById(id)
    }

    suspend fun getCharactersByIds(ids: List<Int>): List<CharacterDto> {
        val idsString = ids.joinToString(",")
        return api.getCharactersByIds(idsString)
    }
}
