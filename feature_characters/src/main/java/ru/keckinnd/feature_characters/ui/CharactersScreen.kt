@file:Suppress("DEPRECATION")

package ru.keckinnd.feature_characters.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.keckinnd.domain.model.Character
import ru.keckinnd.feature_characters.CharactersState
import ru.keckinnd.feature_characters.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
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

    LaunchedEffect(state.items) {
        Log.d("UIDebug", "UI received ${state.items.size} items, filters=${viewModel.getCurrentFilters()}")
    }

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
            var query by remember { mutableStateOf(state.query) }

            LaunchedEffect(query) {
                snapshotFlow { query }
                    .debounce(300)
                    .distinctUntilChanged()
                    .collect { viewModel.onQueryChanged(it) }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Поиск по имени") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Очистить")
                            }
                        }
                    }
                )

                // UI состояния
                when {
                    state.isLoading && state.items.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    }
                    state.error != null -> {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(top = 32.dp),
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
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "Ничего не найдено",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
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
}

@Composable
private fun CharacterGrid(
    items: List<Character>,
    state: CharactersState,
    gridState: LazyGridState,
    onCharacterClick: (Int) -> Unit,
    onRefresh: () -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = onRefresh
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState,
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { char ->
                CharacterCard(character = char, onClick = { onCharacterClick(char.id) })
            }

            if (state.isLoadingMore || state.endReached) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (state.isLoadingMore) {
                            CircularProgressIndicator()
                        } else {
                            Text(
                                text = "Больше нет персонажей",
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}