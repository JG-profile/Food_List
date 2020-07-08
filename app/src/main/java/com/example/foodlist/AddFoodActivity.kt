package com.example.foodlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.properties.Delegates


class AddFoodActivity () : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var editNameView: EditText
    private lateinit var editTypeView: Spinner
    private lateinit var editDateView: EditText
    private lateinit var editServingsView: EditText
    private lateinit var nameSent: String
    private lateinit var typeSent: String
    private lateinit var dateSent: String
    private lateinit var servingsSent: String
    private var idSent by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        editNameView = findViewById(R.id.nameText)
        editTypeView = findViewById(R.id.typeSpinner)
        editDateView = findViewById(R.id.dateText)
        editServingsView = findViewById(R.id.servingsText)

        val spinner: Spinner = findViewById(R.id.typeSpinner)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.food_type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        val extras = intent.extras

        if (extras != null) {
            nameSent = extras.getString(EXTRA_NAME).toString()
            dateSent = extras.getString(EXTRA_DATE).toString()
            typeSent = extras.getString(EXTRA_TYPE).toString()
            servingsSent = extras.getString(EXTRA_SERVINGS).toString()
            idSent = extras.getInt(EXTRA_ID)

            editNameView.setText(nameSent)
            editDateView.setText(dateSent)
            editServingsView.setText(servingsSent)

            var i = 0
            while (i < editTypeView.adapter.count) {
                if (typeSent.equals(editTypeView.adapter.getItem(i).toString())) {
                    editTypeView.setSelection(i)
                    break
                }
                i++
            }

        }

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editNameView.text)||TextUtils.isEmpty(editDateView.text)||TextUtils.isEmpty(editServingsView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val name = editNameView.text.toString()
                val type = editTypeView.selectedItem.toString()
                val date = editDateView.text.toString()
                val servings = editServingsView.text.toString()

                val newFood: Food = Food(0, name, type, date, servings)
                replyIntent.putExtra(EXTRA_NAME, name)
                replyIntent.putExtra(EXTRA_TYPE, type)
                replyIntent.putExtra(EXTRA_DATE, date)
                replyIntent.putExtra(EXTRA_SERVINGS, servings)

                if (idSent != null)
                {
                    replyIntent.putExtra(EXTRA_ID, idSent)
                }

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
         parent.getItemAtPosition(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    companion object {
        const val EXTRA_NAME = "com.example.foodlist.NAME"
        const val EXTRA_TYPE = "com.example.foodlist.TYPE"
        const val EXTRA_DATE = "com.example.foodlist.DATE"
        const val EXTRA_SERVINGS = "com.example.foodlist.SERVINGS"
        const val EXTRA_ID = "com.example.foodlist.ID"
    }
}