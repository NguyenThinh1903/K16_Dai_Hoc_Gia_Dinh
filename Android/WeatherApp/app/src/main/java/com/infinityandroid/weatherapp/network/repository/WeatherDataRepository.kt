package com.infinityandroid.weatherapp.network.repository

import android.annotation.SuppressLint
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.infinityandroid.weatherapp.data.CurrentLocation
import com.infinityandroid.weatherapp.data.RemoteLocation
import com.infinityandroid.weatherapp.data.RemoteWeatherData
import com.infinityandroid.weatherapp.network.api.WeatherAPI

//Kết nối và xử lý dữ liệu từ API, cung cấp cho ViewModel.
@Suppress("DEPRECATION")
class WeatherDataRepository(private val weatherAPI: WeatherAPI) {

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        onSuccess: (currenLocation: CurrentLocation) -> Unit,
        onFailure: () -> Unit
    ) {
        fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location ->
            location ?: onFailure()
            onSuccess(
                CurrentLocation(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
        }.addOnFailureListener { onFailure() }
    }

    @SuppressLint("DEPRECATION")
    fun updateAddressText(
        currenLocation: CurrentLocation,
        geocoder: Geocoder,
    ) : CurrentLocation {
        val laititude = currenLocation.latitude ?: return currenLocation
        val longitude = currenLocation.longitude ?: return currenLocation
        return geocoder.getFromLocation(laititude, longitude, 1)?.let { addresses ->
            val address = addresses[0]
            val addressText = StringBuilder()
            val maxAddressLineIndex = address.maxAddressLineIndex
            addressText.append(address.locality).append(", ")
            addressText.append(address.adminArea).append(", ")
            addressText.append(address.countryName)
            currenLocation.copy(
                location = addressText.toString()
            )
        } ?: currenLocation
    }

    suspend fun searchLocation(query: String): List<RemoteLocation>? {
        val response = weatherAPI.searchLocation(query = query)
        return if(response.isSuccessful) response.body() else null
    }

    suspend fun getWeatherData(latitude: Double, longitude: Double): RemoteWeatherData? {
        val response = weatherAPI.getWeatherData(query = "$latitude,$longitude")
        return if(response.isSuccessful) response.body() else null
    }
}