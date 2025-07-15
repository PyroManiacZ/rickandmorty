package ru.keckinnd.feature_characters.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import ru.keckinnd.domain.model.Gender
import ru.keckinnd.domain.model.Species
import ru.keckinnd.domain.model.Status
import ru.keckinnd.domain.repository.CharacterFilters
import ru.keckinnd.feature_characters.CharactersViewModel
import androidx.compose.foundation.layout.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(
    viewModel: CharactersViewModel,
    onApplyFilters: () -> Unit,
    onBack: () -> Unit
) {
    val current = viewModel.getCurrentFilters()
    var status  by remember { mutableStateOf(current.status) }
    var species by remember { mutableStateOf(current.species) }
    var gender  by remember { mutableStateOf(current.gender) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Фильтры") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.onFiltersChanged(CharacterFilters(status, species, gender))
                    onApplyFilters()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
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
            FilterSection(
                title = "Статус",
                selectedValue = status.name,
                options = Status.entries.map { it.name },
                onOptionSelected = { selected ->
                    status = Status.entries.firstOrNull { it.name == selected } ?: Status.Unknown
                }
            )

            Spacer(Modifier.height(16.dp))

            FilterSection(
                title = "Пол",
                selectedValue = gender.name,
                options = Gender.entries.map { it.name },
                onOptionSelected = { selected ->
                    gender = Gender.entries.firstOrNull { it.name == selected } ?: Gender.Unknown
                }
            )

            Spacer(Modifier.height(16.dp))

            FilterSection(
                title = "Вид",
                selectedValue = species.label,
                options = Species.entries.map { it.label },
                onOptionSelected = { selected ->
                    species = Species.entries.firstOrNull { it.label == selected } ?: Species.Unknown
                }
            )
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    selectedValue: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = selectedValue == option,
                    onClick = { onOptionSelected(if (selectedValue == option) "" else option) },
                    label = { Text(option) },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}
