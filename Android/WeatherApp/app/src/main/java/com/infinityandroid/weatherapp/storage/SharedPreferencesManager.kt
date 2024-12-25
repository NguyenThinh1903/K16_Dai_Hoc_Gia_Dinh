package com.infinityandroid.weatherapp.storage

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.infinityandroid.weatherapp.data.CurrentLocation
//Lưu trữ và truy xuất thông tin vị trí bằng SharedPreferences.
class SharedPreferencesManager(context: Context, private val gson : Gson) {

    private companion object{
        const val PREFS_NAME = "WeatherAppPrefs"
        const val KEY_CURRENT_LOCATION = "currentLocation"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveCurrentLocation(currentLocation: CurrentLocation) {
        val currentLocationJson = gson.toJson(currentLocation)
        sharedPreferences.edit {
            putString(KEY_CURRENT_LOCATION, currentLocationJson)
        }
    }

    fun getCurrentLocation(): CurrentLocation? {
        return sharedPreferences.getString(
            KEY_CURRENT_LOCATION,
            null
        )?.let { currentLocationJson ->
            gson.fromJson(currentLocationJson, CurrentLocation::class.java)
        }
    }
}