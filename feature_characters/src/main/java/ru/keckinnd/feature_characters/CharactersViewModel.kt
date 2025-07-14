package ru.keckinnd.feature_characters

import android.R.attr.id
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
        loadCharacters(id)
    }

    fun loadCharacters(id: Int) {
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
                        items = it.items + list,
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
        _state.update { it.copy(isLoadingMore = true) }
        currentPage++
        loadCharacters(id)
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
        loadCharacters(id)
    }

    fun onSearch(query: String) {
        _state.update { it.copy(query = query) }
    }

    fun search() {
        refresh()
    }

    fun onFiltersChanged(filters: CharacterFilters) {
        currentFilters = filters
        refresh()
    }

    fun retry() {
        if (_state.value.items.isEmpty()) loadCharacters(id)
        else loadNextPage()
    }
}