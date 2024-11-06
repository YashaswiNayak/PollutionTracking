package com.example.hci_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FaqActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)  // Ensure you set the correct layout for this activity
        // Optionally, set a title or customize the toolbar further

    }

    // Handle back button behavior
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()  // This will navigate the user back to the previous screen
        return true
    }
}
