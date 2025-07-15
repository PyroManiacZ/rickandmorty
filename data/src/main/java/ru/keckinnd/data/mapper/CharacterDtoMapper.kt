package ru.keckinnd.data.mapper

import ru.keckinnd.core.network.dto.CharacterDto
import ru.keckinnd.domain.model.*

fun CharacterDto.toDomain(): Character =
    Character(
        id      = id,
        name    = name,
        status  = Status.from(status),
        species = species,
        type    = type,
        gender  = Gender.from(gender),
        origin  = Location(origin.name, origin.url),
        location= Location(location.name, location.url),
        image   = image,
        episode = episode
    )