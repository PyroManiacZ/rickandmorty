package ru.keckinnd.data.repository

import android.util.Log
import ru.keckinnd.domain.repository.CharacterFilters
import ru.keckinnd.data.mapper.toDomain
import ru.keckinnd.data.source.CharactersRemoteDataSource
import ru.keckinnd.domain.model.Character
import ru.keckinnd.domain.repository.CharactersRepository
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharactersRemoteDataSource
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

        val dto = remoteDataSource.getCharacters(
            page    = page,
            name    = nameParam,
            status  = statusParam,
            species = speciesParam,
            type    = null,
            gender  = genderParam
        )
        Log.d("API", "page=$page, results=${dto.results.size}")
        return dto.results.map { it.toDomain() }
    }

    override suspend fun getCharacterById(id: Int): Character =
        remoteDataSource.getCharacterById(id).toDomain()

    override suspend fun getCharactersByIds(ids: List<Int>): List<Character> =
        remoteDataSource.getCharactersByIds(ids).map { it.toDomain() }
}
