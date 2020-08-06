package com.example.grocery.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.grocery.R;

public class StartActivity extends AppCompatActivity {

    Button mulai_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mulai_btn = findViewById(R.id.mulai_btn);
        mulai_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        });
    }
}