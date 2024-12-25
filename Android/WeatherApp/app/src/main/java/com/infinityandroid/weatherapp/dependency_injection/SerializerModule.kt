package com.infinityandroid.weatherapp.dependency_injection

import com.google.gson.Gson
import org.koin.dsl.module
//Cấu hình Gson để chuyển đổi giữa đối tượng Kotlin và JSON.
val seriallizerModule = module {
    single { Gson() }
}