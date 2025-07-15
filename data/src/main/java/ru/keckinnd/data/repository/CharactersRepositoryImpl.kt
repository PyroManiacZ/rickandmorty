package ru.keckinnd.data.repository

import android.util.Log
import ru.keckinnd.data.mapper.toDomain
import ru.keckinnd.data.source.CharacterLocalDataSource
import ru.keckinnd.data.source.CharactersRemoteDataSource
import ru.keckinnd.domain.model.Character
import ru.keckinnd.domain.model.Gender
import ru.keckinnd.domain.model.Species
import ru.keckinnd.domain.model.Status
import ru.keckinnd.domain.repository.CharacterFilters
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

        Log.d("RepoDebug", ">>> getCharacters(page=$page, query='$query', filters=$filters)")

        val nameParam    = query.takeIf    { it.isNotBlank() }
        val statusParam  = filters.status   .takeIf { it != Status.Unknown }?.name?.lowercase()
        val speciesParam = filters.species  .takeIf { it != Species.Unknown }?.name?.lowercase()
        val genderParam  = filters.gender   .takeIf { it != Gender.Unknown }?.name?.lowercase()

        return try {
            // Загрузка с API
            val dto = remoteDataSource.getCharacters(
                page    = page,
                name    = nameParam,
                status  = statusParam,
                species = speciesParam,
                type    = null,
                gender  = genderParam,
            )
            val characters = dto.results.map { it.toDomain() }
            // Сейв в локальный кэш
            localDataSource.saveCharacters(characters)
            characters
        } catch (e: Exception) {
            Log.e("Repository", "Remote failed, fallback to local: ${e.message}")
            // Из локалки без сети
            return localDataSource.getCharacters(
                query   = nameParam,
                filters = filters
            )
        }
    }

    override suspend fun getCharacterById(id: Int): Character =
        remoteDataSource.getCharacterById(id).toDomain()

    override suspend fun getCharactersByIds(ids: List<Int>): List<Character> =
        remoteDataSource.getCharactersByIds(ids).map { it.toDomain() }
}
