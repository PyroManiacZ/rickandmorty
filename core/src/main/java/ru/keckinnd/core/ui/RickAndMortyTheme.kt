package ru.keckinnd.core.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import ru.keckinnd.domain.model.Gender
import ru.keckinnd.domain.model.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RickAndMortyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> if (darkTheme) {
            dynamicDarkColorScheme(context)
        } else {
            dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme(
            primary         = PrimaryDark,
            secondary       = SecondaryDark,
            tertiary        = TertiaryDark,
            background      = BackgroundDark,
            surface         = SurfaceDark,
            surfaceVariant  = SurfaceVariantDark,
            onPrimary       = OnPrimaryDark,
            onSecondary     = OnSecondaryDark,
            onBackground    = OnBackgroundDark,
            onSurface       = OnSurfaceDark
        )

        else -> lightColorScheme(
            primary         = PrimaryLight,
            secondary       = SecondaryLight,
            tertiary        = TertiaryLight,
            background      = BackgroundLight,
            surface         = SurfaceLight,
            surfaceVariant  = SurfaceVariantLight,
            onPrimary       = OnPrimaryLight,
            onSecondary     = OnSecondaryLight,
            onBackground    = OnBackgroundLight,
            onSurface       = OnSurfaceLight
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun statusColor(status: Status): Color = when (status) {
    Status.Alive   -> if (isSystemInDarkTheme()) StatusAliveDark else StatusAliveLight
    Status.Dead    -> if (isSystemInDarkTheme()) StatusDeadDark else StatusDeadLight
    Status.Unknown -> if (isSystemInDarkTheme()) StatusUnknownDark else StatusUnknownLight
}


@Composable
fun genderColor(gender: Gender): Color = when (gender) {
    Gender.Male     -> if (isSystemInDarkTheme()) GenderMaleDark else GenderMaleLight
    Gender.Female   -> if (isSystemInDarkTheme()) GenderFemaleDark else GenderFemaleLight
    Gender.Unknown  -> if (isSystemInDarkTheme()) GenderUnknownDark else GenderUnknownLight
}