package ru.keckinnd.data.repository

import ru.keckinnd.domain.repository.CharacterFilters
import ru.keckinnd.data.mapper.toDomain
import ru.keckinnd.data.source.CharacterLocalDataSource
import ru.keckinnd.data.source.CharactersRemoteDataSource
import ru.keckinnd.domain.model.Character
import ru.keckinnd.domain.repository.CharactersRepository
import javax.inject.Inject

abstract class CharactersRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharactersRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource
) : CharactersRepository {

    override suspend fun getCharacters(
        page: Int,
        query: String,
        filters: CharacterFilters
    ): List<Character> {
        val status = filters.status.takeIf { it.isNotBlank() }
        val species = filters.species.takeIf { it.isNotBlank() }
        val gender = filters.gender.takeIf { it.isNotBlank() }
        val name = query.takeIf { it.isNotBlank() }

        return try {
            val remoteResponse = remoteDataSource.getCharacters(
                page = page,
                name = name,
                status = status,
                species = species,
                type = null,
                gender = gender
            )
            val characters = remoteResponse.results.map { it.toDomain() }
            localDataSource.saveCharacters(characters)
            characters
        } catch (e: Exception) {
            localDataSource.getCharacters(name, filters)
        }
    }

    override suspend fun getCharacterById(id: Int): Character {
        return remoteDataSource.getCharacterById(id).toDomain()
    }

    override suspend fun getCharactersByIds(ids: List<Int>): List<Character> {
        return remoteDataSource.getCharactersByIds(ids).map { it.toDomain() }
    }
}
