package com.example.taskmanagementapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.taskmanagementapp.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NoteDatabaseHelper

    private lateinit var textDateTime: TextView
    private lateinit var buttonDateTime: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val dateTime = binding.textDateTime.text.toString()
            val priority = binding.priority.selectedItem.toString()
            val note = Note(0, title, content, dateTime, priority)
            db.insertNote(note)
            finish()
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
        }

        // Date and Time
        textDateTime = findViewById(R.id.textDateTime)
        buttonDateTime = findViewById(R.id.buttonDateTime)

        val calendar = Calendar.getInstance()

        // Date picker listener
        val dateListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            showTimePicker(calendar)
        }

        buttonDateTime.setOnClickListener {
            DatePickerDialog(
                this,
                dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Spinner
        val priorityId = findViewById<Spinner>(R.id.priority)
        val priorities = arrayOf("High", "Medium", "Low")
        val arrayAdp = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        priorityId.adapter = arrayAdp
    }

    private fun showTimePicker(calendar: Calendar) {
        val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateDateTimeText(calendar)
        }

        TimePickerDialog(
            this,
            timeListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun updateDateTimeText(calendar: Calendar) {
        val dateTimeFormat = "dd-MM-yyyy HH:mm"
        val simpleDateTimeFormat = SimpleDateFormat(dateTimeFormat, Locale.UK)
        textDateTime.text = simpleDateTimeFormat.format(calendar.time)
    }
}
