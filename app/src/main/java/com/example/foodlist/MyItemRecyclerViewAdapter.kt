package com.example.foodlist

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MyItemRecyclerViewAdapter internal constructor(
    context: Context, private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.FoodViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var foods = emptyList<Food>() // Cached copy of words


    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val nameView: TextView = itemView.findViewById(R.id.name)
        val dateView: TextView = itemView.findViewById(R.id.date)
        val servingView: TextView = itemView.findViewById(R.id.servings)
        val typeView: ImageView = itemView.findViewById(R.id.foodType)
        val view: View = itemView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = inflater.inflate(R.layout.fragment_item, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val current = foods[position]

        if (current.type == "Protein")
        {
            holder.typeView.setImageResource(R.drawable.ic_meat)
        }else if (current.type == "Carb")
        {
            holder.typeView.setImageResource(R.drawable.ic_bakery)
        }else
        {
            holder.typeView.setImageResource(R.drawable.ic_vegetable)
        }

        holder.nameView.text = current.name

        val parser = SimpleDateFormat("yyyy/MM/dd")
        val formatter = SimpleDateFormat("yyyy/MM/dd")
        val foodDate = parser.parse(current.date)
        val today = parser.parse(formatter.format(Date()))
        val diffInMillisec: Long = foodDate.time - today.time
        val diffInDays: Long = TimeUnit.MILLISECONDS.toDays(diffInMillisec)
        if (diffInDays < -2)
        {
            holder.view.setBackgroundColor(Color.parseColor("#f06262"))
        }else
        {
            holder.view.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        holder.dateView.text = current.date

        holder.servingView.text = current.servings

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(current)
        }

        holder.itemView.setOnLongClickListener {

            cellClickListener.onCellLongClickListener(current.id, current.name)
            true
        }

    }

    internal fun setFoods(foods: List<Food>) {
        this.foods = foods
        notifyDataSetChanged()
    }

    override fun getItemCount() = foods.size
}

