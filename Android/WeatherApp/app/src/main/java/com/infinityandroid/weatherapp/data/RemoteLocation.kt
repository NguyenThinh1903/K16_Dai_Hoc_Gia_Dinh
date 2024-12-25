package com.infinityandroid.weatherapp.data

// Đại diện cho một vị trí từ xa với thông tin như tên, vùng, quốc gia, vĩ độ, kinh độ
data class RemoteLocation(
    val name: String,
    val region: String? = null,
    val country: String,
    val lat: Double,
    val lon: Double
)
