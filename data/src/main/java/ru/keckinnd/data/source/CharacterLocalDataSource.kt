package ru.keckinnd.data.source

import ru.keckinnd.data.local.dao.CharacterDao
import ru.keckinnd.data.mapper.toEntity
import ru.keckinnd.data.mapper.toDomain
import ru.keckinnd.domain.model.Character
import ru.keckinnd.domain.repository.CharacterFilters
import javax.inject.Inject

class CharacterLocalDataSource @Inject constructor(
    private val characterDao: CharacterDao
) {
    suspend fun saveCharacters(characters: List<Character>) {
        characterDao.insertAll(characters.map { it.toEntity() })
    }

    suspend fun getCharacters(
        query: String?,
        filters: CharacterFilters
    ): List<Character> {
        val status  = filters.status ?: ""
        val species = filters.species ?: ""
        val gender  = filters.gender ?: ""
        return characterDao.search(query, status, species, gender)
            .map { it.toDomain() }
    }
}

