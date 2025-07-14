package ru.keckinnd.domain.model

data class Character(
    val id: Int,
    val name: String,
    val status: Status,
    val species: String,
    val type: String,
    val gender: Gender,
    val origin: Location,
    val location: Location,
    val image: String,
    val episode: List<String>
)

data class Location(
    val name: String,
    val url: String
)
