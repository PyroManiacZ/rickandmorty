package ru.keckinnd.data.repository

import android.util.Log
import ru.keckinnd.domain.repository.CharacterFilters
import ru.keckinnd.data.mapper.toDomain
import ru.keckinnd.data.source.CharacterLocalDataSource
import ru.keckinnd.data.source.CharactersRemoteDataSource
import ru.keckinnd.domain.model.Character
import ru.keckinnd.domain.repository.CharactersRepository
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharactersRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource
) : CharactersRepository {

    override suspend fun getCharacters(
        page: Int,
        query: String,
        filters: CharacterFilters
    ): List<Character> {
        val nameParam    = query.takeIf    { it.isNotBlank() }
        val statusParam  = filters.status.takeIf  { it.isNotBlank() }
        val speciesParam = filters.species.takeIf { it.isNotBlank() }
        val genderParam  = filters.gender.takeIf  { it.isNotBlank() }

        return try {
            val dto = remoteDataSource.getCharacters(
                page    = page,
                name    = nameParam,
                status  = statusParam,
                species = speciesParam,
                type    = null,
                gender  = genderParam
            )
            Log.d("API", "page=$page, results=${dto.results.size}")

            val characters = dto.results.map { it.toDomain() }

            // кэширование
            localDataSource.saveCharacters(characters)

            characters
        } catch (e: Exception) {
            Log.e("Repository", "Failed to get characters from remote, fallback to local: ${e.message}")
            // данные из локалки
            localDataSource.getCharacters(query = nameParam, filters = filters)
        }
    }

    override suspend fun getCharacterById(id: Int): Character =
        try {
            remoteDataSource.getCharacterById(id).toDomain()
        } catch (e: Exception) {
            throw e
        }

    override suspend fun getCharactersByIds(ids: List<Int>): List<Character> =
        try {
            remoteDataSource.getCharactersByIds(ids).map { it.toDomain() }
        } catch (e: Exception) {
            throw e
        }
}
