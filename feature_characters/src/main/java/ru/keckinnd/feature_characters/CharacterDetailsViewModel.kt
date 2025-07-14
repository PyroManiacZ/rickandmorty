package ru.keckinnd.feature_characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.keckinnd.domain.model.Character
import ru.keckinnd.domain.repository.CharactersRepository
import javax.inject.Inject

data class CharacterDetailsState(
    val character: Character? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val repository: CharactersRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterDetailsState())
    val state: StateFlow<CharacterDetailsState> = _state.asStateFlow()

    fun loadCharacter(id: Int) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val character = repository.getCharacterById(id)
                _state.value = CharacterDetailsState(character = character)
            } catch (e: Exception) {
                _state.value = CharacterDetailsState(error = e.message)
            }
        }
    }
}
