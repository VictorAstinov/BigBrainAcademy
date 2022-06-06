package com.example.bigbrainacademy;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.example.bigbrainacademy.databinding.ActivityMainBinding;


// view for the app launch screen
public class MainActivity extends AppCompatActivity implements ActivityInterface {
  private View view;
  private ActivityMainBinding bind;
  // entry point for app
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init_view();
    init_buttons();
    //start_button.setBackgroundColor(Color.RED);
    /*
    // add start button feature (for now just change color to red)
    start_button.setOnclickListener((c) -> start_button.setBackgroundColor(Color.RED))
    .*/
  }
  @Override
  public void init_view() {
    bind = ActivityMainBinding.inflate(getLayoutInflater());
    view = bind.getRoot();
    setContentView(view);
  }
  @Override
  public void init_buttons() {
    Button start_button = findViewById(R.id.start_screen_button);
    start_button.setOnClickListener((x) -> startActivity(new Intent(this, ChoiceScreen.class)));
  }
}