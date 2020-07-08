package com.example.foodlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "food_table")
data class Food (@PrimaryKey (autoGenerate = true) val id: Int,
                 @ColumnInfo(name = "name") val name: String,
                 @ColumnInfo(name = "type") val type: String,
                 @ColumnInfo(name = "date") val date: String,
                 @ColumnInfo(name = "servings") val servings: String)
{
}