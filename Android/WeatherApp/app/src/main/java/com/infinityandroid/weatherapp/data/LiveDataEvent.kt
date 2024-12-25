package com.infinityandroid.weatherapp.data


// lớp quản lý sự kiện chỉ được xử lý một lần, tránh xử lý lặp lại.
data class LiveDataEvent<out T>(private val content: T){

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        }else{
            hasBeenHandled = true
            content
        }
    }
}
