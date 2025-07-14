package ru.keckinnd.data.mapper

import ru.keckinnd.core.network.dto.CharacterDto
import ru.keckinnd.data.local.entity.CharacterEntity
import ru.keckinnd.domain.model.Character
import ru.keckinnd.domain.model.Gender
import ru.keckinnd.domain.model.Location
import ru.keckinnd.domain.model.Status

fun CharacterEntity.toDomain(): Character = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    origin = Location(originName, originUrl),
    location = Location(locationName, locationUrl),
    image = image,
    episode = emptyList()
)


fun Character.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    originName = origin.name,
    originUrl = origin.url,
    locationName = location.name,
    locationUrl = location.url,
    image = image
)

fun CharacterDto.toDomain(): Character = Character(
    id       = id,
    name     = name,
    status   = Status.from(status),
    species  = species,
    type     = type,
    gender   = Gender.from(gender),
    origin   = Location(origin.name, origin.url),
    location = Location(location.name, location.url),
    image    = image,
    episode  = episode
)