package ru.keckinnd.feature_characters.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.keckinnd.data.ui.genderColor
import ru.keckinnd.data.ui.statusColor
import ru.keckinnd.domain.model.Character

@Composable
fun CharacterCard(
    character: Character,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clickable(onClick = onClick)
    ) {
        Column {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${character.species} • ${character.status}",
                    style = MaterialTheme.typography.bodySmall,
                    color = statusColor(character.status)
                )

                Text(
                    text = character.gender.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = genderColor(character.gender)
                )
            }
        }
    }
}
