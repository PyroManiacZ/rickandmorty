package ru.keckinnd.feature_characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.keckinnd.domain.repository.CharacterFilters
import ru.keckinnd.domain.repository.CharactersRepository
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: CharactersRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CharactersState())
    val state: StateFlow<CharactersState> = _state.asStateFlow()

    private val queryFlow = MutableStateFlow("")
    private val filtersFlow = MutableStateFlow(CharacterFilters())

    private var currentPage = 1

    init {
        viewModelScope.launch {
            combine(
                queryFlow.debounce(300).distinctUntilChanged(),
                filtersFlow
            ) { query, filters -> query to filters }
                .collectLatest { (query, filters) ->
                    _state.update { it.copy(query = query) }
                    refreshCharactersInternal(query, filters)
                }
        }
    }

    private fun refreshCharactersInternal(query: String, filters: CharacterFilters) {
        currentPage = 1
        _state.update {
            it.copy(
                items = emptyList(),
                endReached = false,
                isRefreshing = true,
                error = null
            )
        }

        viewModelScope.launch {
            try {
                val list = repository.getCharacters(
                    page = currentPage,
                    query = query,
                    filters = filters
                )
                _state.update {
                    it.copy(
                        items = list,
                        isRefreshing = false,
                        endReached = list.isEmpty(),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isRefreshing = false,
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun loadCharacters() {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val list = repository.getCharacters(
                    page = currentPage,
                    query = _state.value.query,
                    filters = filtersFlow.value
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
        filtersFlow.value = filtersFlow.value
    }

    fun onFiltersChanged(filters: CharacterFilters) {
        filtersFlow.value = filters
    }

    fun getCurrentFilters(): CharacterFilters = filtersFlow.value

    fun onQueryChanged(newQuery: String) {
        queryFlow.value = newQuery
    }

    fun retry() {
        if (_state.value.items.isEmpty()) loadCharacters()
        else loadNextPage()
    }
}
