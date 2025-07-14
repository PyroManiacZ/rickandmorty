package ru.keckinnd.feature_characters.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ru.keckinnd.core.ui.genderColor
import ru.keckinnd.core.ui.statusColor
import ru.keckinnd.domain.model.Character
import ru.keckinnd.feature_characters.CharacterDetailsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    id: Int,
    onBack: () -> Unit,
    viewModel: CharacterDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val character = state.character
    val error = state.error
    val isLoading = state.isLoading

    LaunchedEffect(id) {
        viewModel.loadCharacter(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.character?.name ?: "Детали") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                error != null -> {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                character != null -> {
                    CharacterDetailsContent(character)
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailsContent(character: Character) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.padding(16.dp)) {
            Column(Modifier.padding(16.dp)) {
                InfoRow(
                    label = "Статус",
                    value = character.status.name,
                    color = statusColor(character.status)
                )
                InfoRow(
                    label = "Вид",
                    value = character.species
                )
                InfoRow(
                    label = "Пол",
                    value = character.gender.name,
                    color = genderColor(character.gender)
                )
                InfoRow(
                    label = "Происхождение",
                    value = character.origin.name
                )
                InfoRow(
                    label = "Локация",
                    value = character.location.name
                )
                InfoRow(
                    label = "Эпизоды",
                    value = "${character.episode.size} эпизодов"
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = color
        )
    }
}
