package com.example.bigbrainacademy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bigbrainacademy.databinding.ActivityPracticeScreenBinding;

import java.util.ArrayList;

// view for the practice menu
public class PracticeScreen extends AppCompatActivity implements ActivityInterface {
  ActivityPracticeScreenBinding bind;
  View view;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init_view();
    init_buttons();
  }

  @Override
  public void init_view() {
    bind = ActivityPracticeScreenBinding.inflate(getLayoutInflater());
    view = bind.getRoot();
    setContentView(view);
    startActivity(new Intent(this, WrittenMathScreen.class));
  }

  @Override
  public void init_buttons() {
  }
}