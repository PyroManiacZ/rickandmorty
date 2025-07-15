package ru.keckinnd.domain.repository

import ru.keckinnd.domain.model.Gender
import ru.keckinnd.domain.model.Species
import ru.keckinnd.domain.model.Status

data class CharacterFilters(
    val status: Status = Status.Unknown,
    val species: Species = Species.Unknown,
    val gender: Gender = Gender.Unknown
)