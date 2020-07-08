package com.example.foodlist

interface CellClickListener {
    fun onCellLongClickListener(id: Int, name: String)
    fun onCellClickListener(food: Food)
}