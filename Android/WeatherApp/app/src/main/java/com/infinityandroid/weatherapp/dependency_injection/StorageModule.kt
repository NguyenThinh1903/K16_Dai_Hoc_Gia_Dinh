package com.infinityandroid.weatherapp.dependency_injection

import com.infinityandroid.weatherapp.storage.SharedPreferencesManager
import org.koin.dsl.module
//Cấu hình SharedPreferencesManager để lưu thông tin vị trí người dùng.
val storageModule = module {
    single { SharedPreferencesManager(context = get(), gson = get()) }

}