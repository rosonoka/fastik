package ru.deltadelete.fastikvpn.data

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

/**
 * Объект, абстрагирующий работу со сменой языка между разными версиями Android,
 * т.к. applicationLocales доступен только после Android 13 (Tiramisu).
 */
object LocaleHelper {

    /**
     * Устанавливает язык приложения.
     * Для использования системного языка передайте null или пустую строку.
     *
     * Примеры:
     *   setAppLocale(context, "en")     -> Английский
     *   setAppLocale(context, "ru")     -> Русский
     *   setAppLocale(context, null)     -> Системный
     */
    fun setAppLocale(context: Context, languageTag: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(LocaleManager::class.java)
            localeManager.applicationLocales = if (languageTag.isNullOrEmpty()) {
                LocaleList.getEmptyLocaleList()
            } else {
                LocaleList.forLanguageTags(languageTag)
            }
        } else {
            val localeList = if (languageTag.isNullOrEmpty()) {
                LocaleListCompat.getEmptyLocaleList()
            } else {
                LocaleListCompat.forLanguageTags(languageTag)
            }
            AppCompatDelegate.setApplicationLocales(localeList)
        }
    }


    /**
     * Получает тег текущего языка или null при использовании системного.
     */
    fun getCurrentLanguageTag(context: Context): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(LocaleManager::class.java)
            localeManager.applicationLocales[0]?.toLanguageTag()
        } else {
            AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag()
        }
    }
}