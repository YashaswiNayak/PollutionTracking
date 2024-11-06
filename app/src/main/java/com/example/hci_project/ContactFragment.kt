package com.example.hci_project

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class ContactFragment : Fragment(R.layout.fragment_contact) {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var messageInput: EditText
    private lateinit var sendFeedbackButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        nameInput = view.findViewById(R.id.nameInput)
        emailInput = view.findViewById(R.id.emailInput)
        messageInput = view.findViewById(R.id.messageInput)
        sendFeedbackButton = view.findViewById(R.id.sendFeedbackButton)

        // Set an OnClickListener for the button
        sendFeedbackButton.setOnClickListener {
            // Check if all fields are filled
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val message = messageInput.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && message.isNotEmpty()) {
                // Show a toast message confirming feedback was sent
                Toast.makeText(context, "Feedback sent successfully!", Toast.LENGTH_SHORT).show()

                // Optionally, clear the fields after sending
                nameInput.text.clear()
                emailInput.text.clear()
                messageInput.text.clear()
            } else {
                // Show a toast message asking the user to fill all fields
                Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
