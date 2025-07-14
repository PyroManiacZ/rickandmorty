package ru.keckinnd.domain.model

enum class Gender {
    Male, Female, Unknown;

    companion object {
        fun from(value: String?): Gender {
            return when (value?.lowercase()) {
                "male"   -> Male
                "female" -> Female
                else     -> Unknown
            }
        }
    }
}
