package com.elias.weatherapp.data

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.elias.weatherapp.data.model.AppTheme
import com.elias.weatherapp.data.model.LocationData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class SettingsSaveHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val LAT_KEY = doublePreferencesKey("latitude")
        private val LON_KEY = doublePreferencesKey("longitude")
        private val NAME_KEY = stringPreferencesKey("city_name")
        private val COUNTRY_KEY = stringPreferencesKey("country_name")
        private val THEME_KEY = stringPreferencesKey("app_theme")

    }

    private val dataStore = context.dataStore

    suspend fun saveLocation(location: LocationData) {
        dataStore.edit { preferences ->
            preferences[LAT_KEY] = location.latitude
            preferences[LON_KEY] = location.longitude
            location.name?.let { preferences[NAME_KEY] = it }
            location.country?.let { preferences[COUNTRY_KEY] = it }
        }
    }

    suspend fun getSavedLocation(): LocationData? {
        val preferences = dataStore.data.first()
        val lat = preferences[LAT_KEY]
        val lon = preferences[LON_KEY]
        val name = preferences[NAME_KEY]
        val country = preferences[COUNTRY_KEY]

        return if (lat != null && lon != null) {
            LocationData(
                latitude = lat,
                longitude = lon,
                name = name,
                country = country
            )
        } else null
    }

    suspend fun clearLocation() {
        dataStore.edit { preferences ->
            preferences.remove(LAT_KEY)
            preferences.remove(LON_KEY)
            preferences.remove(NAME_KEY)
            preferences.remove(COUNTRY_KEY)
        }
    }

    suspend fun saveTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

    val themeFlow: Flow<AppTheme> = dataStore.data.map { preferences ->
        val themeName = preferences[THEME_KEY] ?: AppTheme.SYSTEM.name

        AppTheme.entries.find { it.name == themeName } ?: AppTheme.SYSTEM
    }


}
