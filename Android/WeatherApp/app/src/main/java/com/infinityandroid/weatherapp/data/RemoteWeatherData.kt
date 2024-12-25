package com.infinityandroid.weatherapp.data

import com.google.gson.annotations.SerializedName


//Chứa dữ liệu thời tiết từ xa, bao gồm thời tiết hiện tại và dự báo.
data class RemoteWeatherData(
    val current: CurrentWeatherRemote,
    val forecast: ForecastRemote
)

data class CurrentWeatherRemote(
    @SerializedName("temp_c") val temperature: Float,
    val condition: WeatherConditionRemote,
    @SerializedName("wind_kph") val wind: Float,
    val humidity: Int
)

data class ForecastRemote(
    @SerializedName("forecastday") val forecastDays: List<ForcastDayRemote>
)

data class ForcastDayRemote(
    val day: DayRemote,
    val hour: List<ForecastHourRemote>
)

data class DayRemote(
    @SerializedName("daily_chance_of_rain") val chanceOfRain: Int
)

data class ForecastHourRemote(
    val time: String,
    @SerializedName("temp_c") val temperature: Float,
    @SerializedName("feelslike_c") val feelsLikeTemperature: Float,
    val condition: WeatherConditionRemote
)

data class WeatherConditionRemote(
    val icon: String
)
