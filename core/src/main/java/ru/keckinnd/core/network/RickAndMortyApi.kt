package ru.keckinnd.core.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.keckinnd.core.network.dto.CharacterDto
import ru.keckinnd.core.network.dto.CharactersPageDto

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: String? = null
    ): CharactersPageDto

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): CharacterDto

    @GET("character/{ids}")
    suspend fun getCharactersByIds(
        @Path("ids") ids: String
    ): List<CharacterDto>
}
