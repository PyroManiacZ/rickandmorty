package ru.keckinnd.feature_characters

import ru.keckinnd.domain.model.Character

data class CharactersState(
    val items: List<Character> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val endReached: Boolean = false,
    val error: String? = null
)
