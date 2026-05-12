package com.elias.weatherapp.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.elias.weatherapp.data.model.AppTheme
import com.elias.weatherapp.data.model.domain.DisplaySettings
import com.elias.weatherapp.data.model.domain.LocationData
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
        // Coordinates
        private val LAT_KEY = doublePreferencesKey("latitude")
        private val LON_KEY = doublePreferencesKey("longitude")

        // City & Country
        private val NAME_KEY = stringPreferencesKey("city_name")
        private val COUNTRY_KEY = stringPreferencesKey("country_name")

        // Theme
        private val THEME_KEY = stringPreferencesKey("app_theme")

        // Weather Data Preferences
        private val SHOW_WIND = booleanPreferencesKey("show_wind")
        private val SHOW_APPARENT_TEMP = booleanPreferencesKey("show_apparent_temp")
        private val SHOW_WIND_GUSTS = booleanPreferencesKey("show_wind_gusts")
        private val SHOW_HUMIDITY = booleanPreferencesKey("show_humidity")
        private val SHOW_CLOUD_COVER = booleanPreferencesKey("show_cloud_cover")
        private val SHOW_PRECIPITATION = booleanPreferencesKey("show_precipitation")

        private val SHOW_PRESSURE_MSL = booleanPreferencesKey("show_pressure_msl")

        private val SHOW_SURFACE_PRESSURE = booleanPreferencesKey("show_surface_pressure")
        private val SHOW_WIND_DIRECTION = booleanPreferencesKey("show_wind_direction")
        private val SHOW_SNOWFALL = booleanPreferencesKey("show_snowfall")

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

    val displaySettingsFlow: Flow<DisplaySettings> = dataStore.data.map { preferences ->
        DisplaySettings(
            showWind = preferences[SHOW_WIND] ?: true, // NEU
            showApparentTemp = preferences[SHOW_APPARENT_TEMP] ?: true,
            showWindGusts = preferences[SHOW_WIND_GUSTS] ?: true,
            showHumidity = preferences[SHOW_HUMIDITY] ?: true,
            showCloudCover = preferences[SHOW_CLOUD_COVER] ?: true,
            showPrecipitation = preferences[SHOW_PRECIPITATION] ?: true,
            showPressureMsl = preferences[SHOW_PRESSURE_MSL] ?: true,
            showSurfacePressure = preferences[SHOW_SURFACE_PRESSURE] ?: true,
            showWindDirection = preferences[SHOW_WIND_DIRECTION] ?: true,
            showSnowfall = preferences[SHOW_SNOWFALL] ?: true
        )
    }
    suspend fun updateDisplaySetting(keyName: String, isEnabled: Boolean) {
        val key = booleanPreferencesKey(keyName)
        dataStore.edit { preferences ->
            preferences[key] = isEnabled
        }
    }


}
