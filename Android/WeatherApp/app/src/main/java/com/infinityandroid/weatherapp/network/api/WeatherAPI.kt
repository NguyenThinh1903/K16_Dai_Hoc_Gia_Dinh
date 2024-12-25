package com.infinityandroid.weatherapp.network.api

import com.infinityandroid.weatherapp.data.RemoteLocation
import com.infinityandroid.weatherapp.data.RemoteWeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
// Định nghĩa các phương thức API sử dụng Retrofit.

interface WeatherAPI {
    companion object{
        const val BASE_URL = "https://api.weatherapi.com/v1/"
        const val API_KEY = "ffca61e1cdaa4312bdc140418241011"
    }

    @GET("search.json")
    suspend fun searchLocation(
        @Query("key") key: String = API_KEY,
        @Query("q") query: String
    ): Response<List<RemoteLocation>>

    @GET("forecast.json")
    suspend fun getWeatherData(
        @Query("key") key: String = API_KEY,
        @Query("q") query: String
    ): Response<RemoteWeatherData>

}