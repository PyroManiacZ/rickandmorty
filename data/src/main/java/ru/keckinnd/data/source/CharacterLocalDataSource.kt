package ru.keckinnd.data.source

import android.util.Log
import ru.keckinnd.data.local.dao.CharacterDao
import ru.keckinnd.data.mapper.toDomain
import ru.keckinnd.data.mapper.toEntity
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
        val status  = filters.status.name.lowercase()
        val species = filters.species.name.lowercase()
        val gender  = filters.gender.name.lowercase()

        Log.d("LocalDebug", ">>> local search(query=$query, status=$status, species=$species, gender=$gender)")

        return characterDao
            .search(query, status, species, gender)
            .map { it.toDomain() }
    }
}