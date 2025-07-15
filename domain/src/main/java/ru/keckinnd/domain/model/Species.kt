// domain/src/main/java/ru/keckinnd/domain/model/Species.kt
package ru.keckinnd.domain.model

enum class Species {
    Human,
    Alien,
    Humanoid,
    Poopybutthole,
    MythologicalCreature,
    Animal,
    Robot,
    Cronenberg,
    Disease,
    Unknown;

    companion object {
        fun from(value: String?): Species {
            return when (value?.lowercase()) {
                "human"                  -> Human
                "alien"                  -> Alien
                "humanoid"               -> Humanoid
                "poopybutthole"          -> Poopybutthole
                "mythological creature"  -> MythologicalCreature
                "animal"                 -> Animal
                "robot"                  -> Robot
                "cronenberg"             -> Cronenberg
                "disease"                -> Disease
                "unknown"                -> Unknown
                else                     -> Unknown
            }
        }
    }

    val label: String
        get() = when(this) {
            MythologicalCreature -> "Mythological Creature"
            Poopybutthole        -> "Poopybutthole"
            else                 -> name
        }
}
