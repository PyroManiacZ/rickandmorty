package ru.keckinnd.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.keckinnd.data.local.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("""
        SELECT * FROM characters
        WHERE (:query IS NULL OR name LIKE '%' || :query || '%')
          AND (:status = '' OR status = :status)
          AND (:species = '' OR species = :species)
          AND (:gender = '' OR gender = :gender)
    """)
    suspend fun search(
        query: String?,
        status: String,
        species: String,
        gender: String
    ): List<CharacterEntity>
}
