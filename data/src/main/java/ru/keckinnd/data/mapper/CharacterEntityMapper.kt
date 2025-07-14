package ru.keckinnd.data.mapper

import ru.keckinnd.data.local.entity.CharacterEntity
import ru.keckinnd.domain.model.Character
import ru.keckinnd.domain.model.Gender
import ru.keckinnd.domain.model.Location
import ru.keckinnd.domain.model.Status

fun CharacterEntity.toDomain(): Character =
    Character(
        id      = id,
        name    = name,
        status  = Status.from(status),
        species = species,
        type    = type,
        gender  = Gender.from(gender),
        origin  = Location(originName, originUrl),
        location= Location(locationName, locationUrl),
        image   = image,
        episode = emptyList()  // если эпизоды не сохраняете в БД
    )

fun Character.toEntity(): CharacterEntity =
    CharacterEntity(
        id           = id,
        name         = name,
        status       = status.name.lowercase(),
        species      = species,
        type         = type,
        gender       = gender.name.lowercase(),
        originName   = origin.name,
        originUrl    = origin.url,
        locationName = location.name,
        locationUrl  = location.url,
        image        = image
    )
