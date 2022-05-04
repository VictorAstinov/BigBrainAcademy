package com.example.bigbrainacademy;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.bigbrainacademy.databinding.ActivityChoiceScreenBinding;

// view for the choice screen after the app launch screen
public class ChoiceScreen extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityChoiceScreenBinding bind = ActivityChoiceScreenBinding.inflate(getLayoutInflater());
    View view = bind.getRoot();
    setContentView(view);
    Button test_button = findViewById(R.id.test_button);
    Button practice_button = findViewById(R.id.practice_button);
    // This should be refactored to be cleaner for multiple buttons
    test_button.setOnClickListener((c) -> startActivity(new Intent(ChoiceScreen.this, TestScreen.class)));
    practice_button.setOnClickListener((c) -> startActivity(new Intent(ChoiceScreen.this, PracticeScreen.class)));
  }
}