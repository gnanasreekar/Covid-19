package com.rgs.covid_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Feedback extends AppCompatActivity {

    private AppCompatEditText namea;
    private AppCompatEditText phonenumner;
    private AppCompatEditText message;
    private AppCompatButton btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        namea = findViewById(R.id.name);
        phonenumner = findViewById(R.id.phonenumner);
        message = findViewById(R.id.message);
        btSubmit = findViewById(R.id.bt_submit);


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = namea.getText().toString();
                String phone = phonenumner.getText().toString();
                String feedback = message.getText().toString();
                //Checking edit text if its empty
                if (name.isEmpty()) {
                    namea.setError("Provide your Name");
                    namea.requestFocus();
                } else if (phone.isEmpty()) {
                    phonenumner.setError("Provide your Phone!");
                    phonenumner.requestFocus();
                } else if (feedback.isEmpty()) {
                    message.setError("Fill the Field");
                    message.requestFocus();
                } else {
                    //Uploading Data to FBDB
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feedback/" + phone);
                    databaseReference.child("Name").setValue(name);
                    databaseReference.child("Email").setValue(phone);
                    databaseReference.child("Feedback").setValue(feedback);
                    Toast.makeText(Feedback.this, "Thanks for your feedback! ;-)", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
}
