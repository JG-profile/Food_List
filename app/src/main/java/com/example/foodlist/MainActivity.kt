package com.example.foodlist

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CellClickListener {

    private lateinit var foodViewModel: FoodViewModel
    private val newFoodActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = MyItemRecyclerViewAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)


        foodViewModel.getFoodsSorted.observe(this, Observer { foods ->
            // Update the cached copy of the words in the adapter.
            foods?.let { adapter.setFoods(it) }
        })
        foodViewModel.sortBy("name")

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddFoodActivity::class.java)
            startActivityForResult(intent, newFoodActivityRequestCode)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newFoodActivityRequestCode && resultCode == Activity.RESULT_OK) {
            var name: String =""
            var date: String =""
            var type: String =""
            var servings: String =""
            var id: Int = 0
            data?.getStringExtra(AddFoodActivity.EXTRA_NAME)?.let {
                name = it
            }
            data?.getStringExtra(AddFoodActivity.EXTRA_TYPE)?.let {
                type = it
            }
            data?.getStringExtra(AddFoodActivity.EXTRA_DATE)?.let {
                date = it
            }
            data?.getStringExtra(AddFoodActivity.EXTRA_SERVINGS)?.let {
                servings = it
            }
            data?.getIntExtra(AddFoodActivity.EXTRA_ID, 0)?.let {
                id = it
            }
            val food = Food(id, name, type, date, servings)
            if (id == 0)
            {
                foodViewModel.insert(food)
            }else
            {
                foodViewModel.update(food)
            }


        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_name -> {
                foodViewModel.sortBy("name")
                return true
            }
            R.id.action_date -> {
                foodViewModel.sortBy("date")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCellLongClickListener(id: Int, name: String) {

        lateinit var dialog:AlertDialog

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this)

        // Set a title for alert dialog
        builder.setTitle("Delete Food")

        // Set a message for alert dialog
        builder.setMessage("Would you like to delete the " + name + "?")


        // On click listener for dialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{_,which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> foodViewModel.deleteSelected(id)
            }
        }

        // Set the alert dialog positive/yes button
        builder.setPositiveButton("YES",dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNegativeButton("NO",dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNeutralButton("CANCEL",dialogClickListener)

        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

    override fun onCellClickListener(food: Food)
    {
        val intent = Intent(this@MainActivity, AddFoodActivity::class.java)
        intent.putExtra(AddFoodActivity.EXTRA_NAME, food.name)
        intent.putExtra(AddFoodActivity.EXTRA_TYPE, food.type)
        intent.putExtra(AddFoodActivity.EXTRA_DATE, food.date)
        intent.putExtra(AddFoodActivity.EXTRA_SERVINGS, food.servings)
        intent.putExtra(AddFoodActivity.EXTRA_ID, food.id)
        startActivityForResult(intent, newFoodActivityRequestCode)
    }

}


// Extension function to show toast message
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}