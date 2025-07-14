package ru.keckinnd.data.source

import ru.keckinnd.domain.model.Character
import ru.keckinnd.domain.repository.CharacterFilters
import ru.keckinnd.data.local.dao.CharacterDao
import ru.keckinnd.data.mapper.toDomain
import ru.keckinnd.data.mapper.toEntity
import javax.inject.Inject

class CharacterLocalDataSource @Inject constructor(
    private val characterDao: CharacterDao
) {

    suspend fun saveCharacters(characters: List<Character>) {
        val entities = characters.map { it.toEntity() }
        characterDao.insertAll(entities)
    }

    suspend fun getCharacters(
        query: String?,
        filters: CharacterFilters
    ): List<Character> {
        val status = filters.status
        val species = filters.species
        val gender = filters.gender

        val entities = characterDao.search(query, status, species, gender)
        return entities.map { it.toDomain() }
    }
}
