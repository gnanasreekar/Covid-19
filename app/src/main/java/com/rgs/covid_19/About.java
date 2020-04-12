package com.rgs.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Github_rgs
        ImageView github_rgs = findViewById(R.id.github_rgs);
        github_rgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.github.com/gnanasreekar")));
            }
        });

        //Linkedin_rgs
        ImageView linkedin_rgs = findViewById(R.id.linkedin_rgs);
        linkedin_rgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/gnana-sreekar/")));
            }
        });

        //Mail_rgs
        ImageButton mail_rgs = findViewById(R.id.mail_rgs);
        mail_rgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "gnanasreekar@pm.me", null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        //Mail_rgs
        ImageButton web_rgs = findViewById(R.id.web_rgs);
        web_rgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gnanasreekar.com")));
            }
        });
    }
}
