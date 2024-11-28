package com.codebee.mytasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class TermsOfSeviceActivity extends AppCompatActivity {
    Button GoBack;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_service);

        GoBack = findViewById(R.id.GoBackBtn);

        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsOfSeviceActivity.super.onBackPressed();
            }
        });
    }
}
