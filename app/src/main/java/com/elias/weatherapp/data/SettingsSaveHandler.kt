package com.elias.weatherapp.data

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.elias.weatherapp.data.model.LocationData
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsSaveHandler(private val context: Context) {

    companion object {
        val LAT_KEY = doublePreferencesKey("latitude")
        val LON_KEY = doublePreferencesKey("longitude")
    }

    private val dataStore = context.dataStore

    suspend fun saveLocation(lat: Double, lon: Double) {
        dataStore.edit {
            it[LAT_KEY] = lat
            it[LON_KEY] = lon
        }
    }

    suspend fun getSavedLocation(): LocationData? {
        val preferences = dataStore.data.first()
        val lat = preferences[LAT_KEY]
        val lon = preferences[LON_KEY]

        return if (lat != null && lon != null) {
            LocationData(
                latitude = lat,
                longitude = lon
            )
        } else null
    }
}