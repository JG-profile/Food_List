package com.example.foodlist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Food::class), version = 1, exportSchema = false)
public abstract class FoodRoomDatabase : RoomDatabase() {

    abstract fun foodDao(): FoodDao

    private class FoodDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {


        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.foodDao())
                }
            }
        }

        suspend fun populateDatabase(foodDao: FoodDao) {
            // Delete all content here.
            foodDao.deleteAll()

            // Add sample words.
            var food = Food(0, "test","Carb","2020/06/25","2")
            foodDao.insert(food)
            food = Food(0, "chicken","Protein","2020/06/20","5")
            foodDao.insert(food)
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: FoodRoomDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope
        ): FoodRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodRoomDatabase::class.java,
                    "word_database"
                ).addCallback(FoodDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}