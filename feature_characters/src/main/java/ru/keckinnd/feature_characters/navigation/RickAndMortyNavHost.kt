package ru.keckinnd.feature_characters.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavBackStackEntry
import ru.keckinnd.feature_characters.ui.CharacterDetailsScreen
import ru.keckinnd.feature_characters.ui.CharactersScreen
import ru.keckinnd.feature_characters.ui.FiltersScreen
import ru.keckinnd.feature_characters.CharactersViewModel
import ru.keckinnd.feature_characters.CharacterDetailsViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RickAndMortyNavHost(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "characters") {

        composable("characters") { backStackEntry: NavBackStackEntry ->
            val charactersViewModel: CharactersViewModel =
                hiltViewModel(backStackEntry)

            CharactersScreen(
                darkTheme        = darkTheme,
                onToggleTheme    = onToggleTheme,
                onCharacterClick = { id -> navController.navigate("character/$id") },
                onFiltersClick   = { navController.navigate("filters") },
                viewModel        = charactersViewModel
            )
        }

        composable("filters") { backStackEntry: NavBackStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("characters")
            }
            val charactersViewModel: CharactersViewModel =
                hiltViewModel(parentEntry)

            FiltersScreen(
                viewModel       = charactersViewModel,
                onApplyFilters  = { navController.popBackStack() },
                onBack          = { navController.popBackStack() }
            )
        }

        composable(
            route = "character/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry: NavBackStackEntry ->
            val detailsViewModel: CharacterDetailsViewModel =
                hiltViewModel(backStackEntry)

            CharacterDetailsScreen(
                id        = backStackEntry.arguments!!.getInt("id"),
                onBack    = { navController.popBackStack() },
                viewModel = detailsViewModel
            )
        }
    }
}

