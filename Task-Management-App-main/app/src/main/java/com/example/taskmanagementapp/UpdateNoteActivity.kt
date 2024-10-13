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
import com.example.taskmanagementapp.databinding.ActivityUpdateNoteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private lateinit var updateTextDateTime: TextView
private lateinit var updateButtonDateTime: Button

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NoteDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id",-1)
        if(noteId == -1){
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditText.setText(note.content)
        binding.updateTextDateTime.setText(note.date) // Set the date text instead of content text
        val priorityArray = resources.getStringArray(R.array.priority_array)
        val priorityIndex = priorityArray.indexOf(note.priority)

        // Setting up spinner
        val priorityId = binding.updatePriority
        val priorities = arrayOf("High", "Medium", "Low")
        val arrayAdp = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        priorityId.adapter = arrayAdp

        binding.updatePriority.setSelection(priorityIndex)

        binding.updateSaveButton.setOnClickListener{
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val newDateTime = binding.updateTextDateTime.text.toString()
            val newPriority = binding.updatePriority.selectedItem.toString()
            val updatedNote = Note(noteId, newTitle, newContent, newDateTime, newPriority) // Preserve date and priority
            db.updateNote(updatedNote)
            finish()
            Toast.makeText(this,"Changes Saved",Toast.LENGTH_SHORT).show()
        }

        // Setting up date and time picker
        updateTextDateTime = findViewById(R.id.updateTextDateTime)
        updateButtonDateTime = findViewById(R.id.updateButtonDateTime)
        val calendar = Calendar.getInstance()
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            showTimePicker(calendar)
        }
        updateButtonDateTime.setOnClickListener {
            DatePickerDialog(
                this,
                dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
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
        updateTextDateTime.text = simpleDateTimeFormat.format(calendar.time)
    }
}
