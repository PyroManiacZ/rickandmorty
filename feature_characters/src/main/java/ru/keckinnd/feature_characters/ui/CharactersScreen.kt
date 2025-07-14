package ru.keckinnd.feature_characters.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import ru.keckinnd.domain.model.Character
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.keckinnd.feature_characters.CharactersState
import ru.keckinnd.feature_characters.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onCharacterClick: (Int) -> Unit,
    onFiltersClick: () -> Unit,
    viewModel: CharactersViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val gridState = rememberLazyGridState()

    // Пагинация по скроллу
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { index ->
                if (index != null &&
                    index >= state.items.size - 3 &&
                    !state.endReached &&
                    !state.isLoadingMore
                ) {
                    viewModel.loadNextPage()
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Персонажи") },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (darkTheme)
                                Icons.Outlined.LightMode
                            else
                                Icons.Outlined.DarkMode,
                            contentDescription = "Toggle theme"
                        )
                    }
                    IconButton(onClick = onFiltersClick) {
                        Icon(
                            imageVector = Icons.Filled.FilterList,
                            contentDescription = "Filters"
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
                state.isLoading && state.items.isEmpty() -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                state.error != null -> {
                    // Простая заглушка ErrorState
                    Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Ошибка: ${state.error}")
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.retry() }) {
                            Text("Повторить")
                        }
                    }
                }
                state.items.isEmpty() -> {
                    Text(
                        text = "Ничего не найдено",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    CharacterGrid(
                        items = state.items,
                        state = state,
                        gridState = gridState,
                        onCharacterClick = onCharacterClick,
                        onRefresh = viewModel::refresh
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterGrid(
    items: List<Character>,
    state: CharactersState,
    gridState: LazyGridState,
    onCharacterClick: (Int) -> Unit,
    onRefresh: () -> Unit
) {
    // 1. Создаём state для pullRefresh
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = onRefresh
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            // 2. Привязываем pullRefresh к Box
            .pullRefresh(pullRefreshState)
    ) {
        // 3. Ваш LazyVerticalGrid как раньше
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState,
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { char ->
                CharacterCard(character = char, onClick = { onCharacterClick(char.id) })
            }
            if (state.isLoadingMore || state.endReached) {
                item(span = { LazyGridItemSpan(maxLineSpan) }) {
                    if (state.isLoadingMore) {
                        CircularProgressIndicator(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    } else {
                        Text(
                            "Больше нет персонажей",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = state.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}