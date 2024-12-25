package com.infinityandroid.weatherapp.fragments.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinityandroid.weatherapp.data.RemoteLocation
import com.infinityandroid.weatherapp.network.repository.WeatherDataRepository
import kotlinx.coroutines.launch
//Quản lý dữ liệu tìm kiếm vị trí.
class LocationViewModel(private val weatherDataRepository: WeatherDataRepository) : ViewModel() {

    private val _searchResult = MutableLiveData<SearchResultDataState>()
    val searchResult: LiveData<SearchResultDataState> = _searchResult

    fun searchLocation(query: String) {
        viewModelScope.launch {
            emitSearchReasultUIState(isLoading = true)
            val searchResult = weatherDataRepository.searchLocation(query)
            if(searchResult.isNullOrEmpty()){
                emitSearchReasultUIState(error = "No locations found")
            }else{
                emitSearchReasultUIState(locations = searchResult)
            }
        }
    }

    private fun emitSearchReasultUIState(
        isLoading: Boolean = false,
        locations: List<RemoteLocation>? = null,
        error: String? = null
    ){
        val searchResultDataState = SearchResultDataState(isLoading, locations, error)
        _searchResult.value = searchResultDataState

    }

    data class SearchResultDataState(
        val isLoading: Boolean,
        val locations: List<RemoteLocation>?,
        val error: String?
    )
}