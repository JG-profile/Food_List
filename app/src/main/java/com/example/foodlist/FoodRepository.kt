package com.example.foodlist

import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class FoodRepository(private val foodDao: FoodDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allFoodsByName: LiveData<List<Food>> = foodDao.getFoodsByName()
    val allFoodsByDate: LiveData<List<Food>> = foodDao.getFoodsByDate()

    suspend fun insert(food: Food) {
        foodDao.insert(food)
    }

    suspend fun update(food: Food) {
        foodDao.update(food)
    }

    suspend fun deleteSelected(id: Int){
        foodDao.delete(id)
    }

    fun sortData (sort: String): LiveData<List<Food>>
    {
        return foodDao.getFoodsSorted(sort)
    }
}