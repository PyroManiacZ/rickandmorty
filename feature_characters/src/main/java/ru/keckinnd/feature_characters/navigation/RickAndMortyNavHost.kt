package ru.keckinnd.feature_characters.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.keckinnd.feature_characters.ui.CharacterDetailsScreen
import ru.keckinnd.feature_characters.ui.CharactersScreen
import ru.keckinnd.feature_characters.ui.FiltersScreen

@Composable
fun RickAndMortyNavHost(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "characters") {
        composable("characters") {
            CharactersScreen(
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme,
                onCharacterClick = { id -> navController.navigate("character/$id") },
                onFiltersClick = { navController.navigate("filters") }
            )
        }

        composable(
            route = "character/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            CharacterDetailsScreen(
                id = it.arguments?.getInt("id") ?: 0,
                onBack = { navController.popBackStack() }
            )
        }

        composable("filters") {
            FiltersScreen(
                onApplyFilters = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
