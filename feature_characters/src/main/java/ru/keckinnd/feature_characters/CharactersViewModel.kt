package ru.keckinnd.feature_characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.keckinnd.domain.repository.CharacterFilters
import ru.keckinnd.domain.repository.CharactersRepository
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: CharactersRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CharactersState())
    val state: StateFlow<CharactersState> = _state.asStateFlow()

    private var currentPage = 1
    private var currentFilters = CharacterFilters()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val list = repository.getCharacters(
                    page = currentPage,
                    query = _state.value.query,
                    filters = currentFilters
                )
                _state.update {
                    it.copy(
                        items = if (currentPage == 1) list else it.items + list,
                        endReached = list.isEmpty(),
                        isLoading = false,
                        isLoadingMore = false,
                        isRefreshing = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        isRefreshing = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun loadNextPage() {
        if (_state.value.isLoadingMore || _state.value.endReached) return
        currentPage++
        _state.update { it.copy(isLoadingMore = true) }
        loadCharacters()
    }

    fun refresh() {
        currentPage = 1
        _state.update {
            it.copy(
                items = emptyList(),
                endReached = false,
                isRefreshing = true
            )
        }
        loadCharacters()
    }

    fun onFiltersChanged(filters: CharacterFilters) {
        currentFilters = filters
        refresh()
    }

    fun getCurrentFilters(): CharacterFilters = currentFilters

    fun retry() {
        if (_state.value.items.isEmpty()) loadCharacters()
        else loadNextPage()
    }
}
