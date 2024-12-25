package com.infinityandroid.weatherapp.fragments.location

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinityandroid.weatherapp.data.RemoteLocation
import com.infinityandroid.weatherapp.databinding.ItemContainerLocationBinding
//Quản lý hiển thị danh sách vị trí từ xa trong RecyclerView
class LocationAdapter(
    private val onLocationClicked: (RemoteLocation) -> Unit
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private val location = mutableListOf<RemoteLocation>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<RemoteLocation>){
        location.clear()
        location.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(
            ItemContainerLocationBinding.inflate(
               LayoutInflater.from(parent.context)
                ,parent,
                false

            )
        )
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(remoteLocation = location[position])
    }

    override fun getItemCount(): Int {
        return location.size
    }

    inner class LocationViewHolder(
        private val binding: ItemContainerLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(remoteLocation: RemoteLocation) {
            with(remoteLocation){
                val location = "$name, ${region ?: ""}, $country"
                binding.textRemoteLocation.text = location
                binding.root.setOnClickListener {onLocationClicked(remoteLocation)}
            }
        }
    }
}
