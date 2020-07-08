package com.example.foodlist

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private val allFoodsByName: LiveData<List<Food>>
    private val allFoodsByDate: LiveData<List<Food>>


    init {
        val foodsDao = FoodRoomDatabase.getDatabase(application, viewModelScope).foodDao()
        repository = FoodRepository(foodsDao)
        allFoodsByName = repository.allFoodsByName
        allFoodsByDate = repository.allFoodsByDate
    }

    private val nameQueryLiveData = MutableLiveData<String>()

    val getFoodsSorted: LiveData<List<Food>> = Transformations.switchMap(nameQueryLiveData){
            sort -> repository.sortData(sort)
    }

     fun sortBy(sort: String){
         nameQueryLiveData.value = sort
    }


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(food: Food) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(food)
    }

    fun update(food: Food) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(food)
    }

    fun deleteSelected(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteSelected(id)
    }
}