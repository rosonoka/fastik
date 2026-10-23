package ru.deltadelete.fastikvpn.ui.theme

import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.deltadelete.fastikvpn.R
import javax.inject.Singleton

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = bgMainDark,
    surface = bgMenuDark,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = bgMainLight,
    surface = bgMenuLight,
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
)

enum class AppTheme(
    val translationKey: Int,
    val icon: ImageVector,
) {
    LIGHT(R.string.light_theme, Icons.Filled.LightMode),
    DYNAMIC_LIGHT(R.string.light_dynamic_theme, Icons.Filled.LightMode),
    DARK(R.string.dark_theme, Icons.Filled.DarkMode),
    DYNAMIC_DARK(R.string.dark_dynamic_theme, Icons.Filled.DarkMode),
    SYSTEM(R.string.system_theme, Icons.Filled.Contrast),
    DYNAMIC_SYSTEM(R.string.system_dynamic_theme, Icons.Filled.Contrast);

    fun toPlain(): AppTheme {
        return when (this) {
            DYNAMIC_LIGHT -> LIGHT
            DYNAMIC_DARK -> DARK
            DYNAMIC_SYSTEM -> SYSTEM
            else -> this
        }
    }

    fun toDynamic(): AppTheme {
        return when (this) {
            LIGHT -> DYNAMIC_LIGHT
            DARK -> DYNAMIC_DARK
            SYSTEM -> DYNAMIC_SYSTEM
            else -> this
        }
    }

    val isDynamic: Boolean
        get() {
            return when (this) {
                DYNAMIC_LIGHT -> true
                DYNAMIC_SYSTEM -> true
                DYNAMIC_DARK -> true
                else -> false
            }
        }
}

@Composable
fun FastikVPNTheme(
    colorTheme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (colorTheme.isDynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = LocalContext.current
            when (colorTheme) {
                AppTheme.DYNAMIC_LIGHT -> dynamicLightColorScheme(context)
                AppTheme.DYNAMIC_DARK -> dynamicDarkColorScheme(context)
                AppTheme.DYNAMIC_SYSTEM -> if (isSystemInDarkTheme()) {
                    dynamicDarkColorScheme(context)
                } else {
                    dynamicLightColorScheme(context)
                }

                else -> throw IllegalStateException("Got plain theme when isDynamic() is true.")
            }
        } else {
            when (colorTheme.toPlain()) {
                AppTheme.SYSTEM -> if (isSystemInDarkTheme()) {
                    DarkColorScheme
                } else {
                    LightColorScheme
                }

                AppTheme.LIGHT -> LightColorScheme
                AppTheme.DARK -> DarkColorScheme
                else -> throw IllegalStateException("Got dynamic theme after toPlain() call.")
            }
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            content()
        }
    )
}
