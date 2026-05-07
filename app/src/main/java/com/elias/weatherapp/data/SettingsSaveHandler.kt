package com.elias.weatherapp.data

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.elias.weatherapp.data.model.LocationData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class SettingsSaveHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        val LAT_KEY = doublePreferencesKey("latitude")
        val LON_KEY = doublePreferencesKey("longitude")
        val NAME_KEY = stringPreferencesKey("city_name")
        val COUNTRY_KEY = stringPreferencesKey("country_name")

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
}
