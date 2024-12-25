package com.infinityandroid.weatherapp.fragments.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.infinityandroid.weatherapp.data.CurrentLocation
import com.infinityandroid.weatherapp.data.CurrentWeather
import com.infinityandroid.weatherapp.data.Forcecast
import com.infinityandroid.weatherapp.data.WeatherData
import com.infinityandroid.weatherapp.databinding.ItemContainerCurrentLocationBinding
import com.infinityandroid.weatherapp.databinding.ItemContainerCurrentWeatherBinding
import com.infinityandroid.weatherapp.databinding.ItemContainerForecastBinding
//Quản lý hiển thị dữ liệu thời tiết trong RecyclerView.
class WeatherDataAdapter (
    private val onLocationClicked:() -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private companion object{
        const val INDEX_CURRENT_LOCATION = 0
        const val INDEX_CURRENT_WEATHER = 1
        const val INDEX_FORECAST = 2
    }

    private val weatherData = mutableListOf<WeatherData>()

    fun setCurrenLocation(currentLocation: CurrentLocation){
        if(weatherData.isEmpty()){
            weatherData.add(INDEX_CURRENT_LOCATION, currentLocation)
            notifyItemInserted(INDEX_CURRENT_LOCATION)
        } else {
            weatherData[INDEX_CURRENT_LOCATION] = currentLocation
            notifyItemChanged(INDEX_CURRENT_LOCATION)
        }
    }

    fun setCurrentWeather(currentWeather: CurrentWeather){
        if(weatherData.getOrNull(INDEX_CURRENT_WEATHER) != null){
            weatherData[INDEX_CURRENT_WEATHER] = currentWeather
            notifyItemChanged(INDEX_CURRENT_WEATHER)
        } else {
            weatherData.add(INDEX_CURRENT_WEATHER, currentWeather)
            notifyItemInserted(INDEX_CURRENT_WEATHER)
        }
    }


    fun setForecastData(forecast: List<Forcecast>){
        weatherData.removeAll { it is Forcecast }
        notifyItemRangeRemoved(INDEX_FORECAST, weatherData.size)
        weatherData.addAll(INDEX_FORECAST, forecast)
        notifyItemRangeChanged(INDEX_FORECAST, weatherData.size)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            INDEX_CURRENT_LOCATION -> CurrentLocationViewHolder(
                ItemContainerCurrentLocationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            INDEX_FORECAST -> ForecastViewHolder(
                ItemContainerForecastBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> CurrentWeatherViewHolder(
                ItemContainerCurrentWeatherBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is CurrentLocationViewHolder -> holder.bind(weatherData[position] as CurrentLocation)
            is CurrentWeatherViewHolder -> holder.bind(weatherData[position] as CurrentWeather)
            is ForecastViewHolder -> holder.bind(weatherData[position] as Forcecast)
        }
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    override fun getItemViewType(position: Int): Int{
        return when(weatherData[position]){
            is CurrentLocation -> INDEX_CURRENT_LOCATION
            is CurrentWeather -> INDEX_CURRENT_WEATHER
            is Forcecast -> INDEX_FORECAST
        }
    }

    inner class CurrentLocationViewHolder(
        private val binding: ItemContainerCurrentLocationBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(currentLocation: CurrentLocation){
            with (binding){
                textCurrentDate.text = currentLocation.date
                textCurrentLocation.text = currentLocation.location
                imageCurrentLocation.setOnClickListener { onLocationClicked()}
                textCurrentLocation.setOnClickListener { onLocationClicked()}

            }
        }
    }

    inner class CurrentWeatherViewHolder(
        private val binding: ItemContainerCurrentWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(currentWeather: CurrentWeather){
            with (binding){
                imageIcon.load("https:${currentWeather.icon}") { crossfade(true)}
                textTemperature.text = String.format("/%s\u00B0C", currentWeather.temperature)
                textWind.text = String.format("%s km/h", currentWeather.wind)
                textHumidity.text = String.format("%s%%", currentWeather.humidity)
                textChanceOfRain.text = String.format("%s%%", currentWeather.chanceOfRain)
            }
        }
    }

    inner class ForecastViewHolder(
        private val binding: ItemContainerForecastBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(forcecast: Forcecast){
            with (binding){
                textTime.text = forcecast.time
                textTemperature.text = String.format("%s\u00B0C", forcecast.temperature)
                textFeelsLikeTemperature.text = String.format("Feels like %s\u00B0C", forcecast.feelsLikeTemperature)
                imageIcon.load("https:${forcecast.icon}") { crossfade(true)}
            }
        }
    }
}