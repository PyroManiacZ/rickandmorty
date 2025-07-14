package ru.keckinnd.feature_characters.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.keckinnd.domain.repository.CharacterFilters
import ru.keckinnd.domain.model.Gender
import ru.keckinnd.domain.model.Status
import ru.keckinnd.feature_characters.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(
    onApplyFilters: (CharacterFilters) -> Unit,
    onBack: () -> Unit,
    viewModel: CharactersViewModel = hiltViewModel()
) {
    val current = viewModel.getCurrentFilters()
    var status by remember { mutableStateOf(current.status) }
    var species by remember { mutableStateOf(current.species) }
    var gender by remember { mutableStateOf(current.gender) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Фильтры") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.onFiltersChanged(CharacterFilters(status, species, gender))
                    onApplyFilters(CharacterFilters(status, species, gender))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Применить")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Статус", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
            Status.values().forEach {
                FilterChip(
                    selected = status == it.value,
                    onClick = { status = if (status == it.value) "" else it.value },
                    label = { Text(it.value) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            Text("Пол", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
            Gender.values().forEach {
                FilterChip(
                    selected = gender == it.value,
                    onClick = { gender = if (gender == it.value) "" else it.value },
                    label = { Text(it.value) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            // Если нужны другие фильтры — добавьте здесь
        }
    }
}
