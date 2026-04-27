package com.example.lab10

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab10.databinding.ItemCarBinding

class CarAdapter(private var carList: List<Car>) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    class CarViewHolder(val binding: ItemCarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = ItemCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        with(holder.binding) {
            tvCarName.text = "${car.brand} ${car.model}"
            tvCarYear.text = car.year.toString()
            tvCarDescription.text = car.description
            tvCarCost.text = car.cost
            ivCarPhoto.setImageResource(car.imageResId)
        }
    }

    fun updateList(newList: List<Car>) {
        this.carList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = carList.size
}