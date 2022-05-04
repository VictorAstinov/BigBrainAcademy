package com.example.bigbrainacademy;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.bigbrainacademy.databinding.ActivityMainBinding;


// view for the app launch screen
public class MainActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // create binding layout
    ActivityMainBinding bind = ActivityMainBinding.inflate(getLayoutInflater());
    View view = bind.getRoot();
    setContentView(view);
    Button start_button = findViewById(R.id.start_screen_button);
    //start_button.setBackgroundColor(Color.RED);
    /*
    // add start button feature (for now just change color to red)
    start_button.setOnclickListener((c) -> start_button.setBackgroundColor(Color.RED))
    .*/

    // this uses a lambda with the onClickListener to go to the next activity screen
    start_button.setOnClickListener((x) -> startActivity(new Intent(MainActivity
    .this, ChoiceScreen.class)));

  }
}