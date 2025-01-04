package org.giuliohome.weatherforecastapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "weather_preferences")

object WeatherPreferences {
    private val CITY_KEY = stringPreferencesKey("city")
    private val COUNTRY_KEY = stringPreferencesKey("country")

    suspend fun saveCityAndCountry(context: Context, city: String, country: String) {
        context.dataStore.edit { preferences ->
            preferences[CITY_KEY] = city
            preferences[COUNTRY_KEY] = country
        }
    }

    suspend fun getCityAndCountry(context: Context): Pair<String, String> {
        val preferences = context.dataStore.data.first()
        val city = preferences[CITY_KEY] ?: ""
        val country = preferences[COUNTRY_KEY] ?: ""
        return city to country
    }
}
