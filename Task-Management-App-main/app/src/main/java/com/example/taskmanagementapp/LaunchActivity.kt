package com.example.taskmanagementapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanagementapp.databinding.ActivityLaunchBinding

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Listen for click on the background layout
        binding.root.setOnClickListener {
            // Create an intent to navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // Finish current activity to prevent going back to it when back button is pressed
            finish()
        }
    }
}
