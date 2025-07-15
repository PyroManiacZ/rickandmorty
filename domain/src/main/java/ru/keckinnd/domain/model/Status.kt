package ru.keckinnd.domain.model

enum class Status {
    Alive, Dead, Unknown;

    companion object {
        fun from(value: String?): Status {
            return when (value?.lowercase()) {
                "alive" -> Alive
                "dead"  -> Dead
                else    -> Unknown
            }
        }
    }
}