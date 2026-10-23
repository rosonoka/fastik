package ru.deltadelete.fastikvpn.data.theme

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.deltadelete.fastikvpn.ui.theme.AppTheme

private val Context.dataStore by preferencesDataStore(name = "settings")

/**
 *
 */
class ThemeRepository(
    private val context: Context
) {
    private val themeKey = stringPreferencesKey("app_theme")
    val themeFlow: Flow<AppTheme> = context.dataStore.data.map { preferences ->
        val themeString = preferences[themeKey] ?: AppTheme.SYSTEM.name
        try {
            AppTheme.valueOf(themeString)
        } catch (e: IllegalArgumentException) {
            Log.e(
                ThemeRepository::class.simpleName,
                "Failed to parse value from settings key `app_theme` as ${AppTheme::class.qualifiedName}",
                e
            )
            AppTheme.SYSTEM
        }
    }

    suspend fun saveTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = theme.name
        }
    }
}