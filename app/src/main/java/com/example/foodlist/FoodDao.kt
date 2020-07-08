package com.example.foodlist

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FoodDao {
    @Query("SELECT * from food_table ORDER BY name ASC")
    fun getFoodsByName(): LiveData<List<Food>>

    @Query("SELECT * from food_table ORDER BY date ASC")
    fun getFoodsByDate(): LiveData<List<Food>>

    @Query("SELECT * FROM food_table ORDER BY CASE :sort WHEN 'name' THEN name ELSE date END COLLATE NOCASE ASC")
    fun getFoodsSorted(sort: String): LiveData<List<Food>>

    /*@Query("SELECT * from food_table ORDER BY type ASC")
    fun getFoodsByType(): LiveData<List<Food>>

    @Query("SELECT * from food_table WHERE type = 'Carb'")
    fun getFoodsCarb(): LiveData<List<Food>>

    @Query("SELECT * from food_table WHERE type = 'Veg'")
    fun getFoodsVeg(): LiveData<List<Food>>

    @Query("SELECT * from food_table WHERE type = 'Protein'")
    fun getFoodsProtein(): LiveData<List<Food>>*/

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(food: Food)

    @Update
    suspend fun update(food: Food)

    @Query("DELETE FROM food_table")
    suspend fun deleteAll()

    @Query("DELETE FROM food_table WHERE id = :id")
    suspend fun delete(id: Int)
}