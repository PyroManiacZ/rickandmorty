package ru.keckinnd.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.keckinnd.domain.model.Gender
import ru.keckinnd.domain.model.Status

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: Status,
    val species: String,
    val gender: Gender,
    val originName: String,
    val originUrl: String,
    val locationName: String,
    val locationUrl: String,
    val image: String,
    val type: String
)