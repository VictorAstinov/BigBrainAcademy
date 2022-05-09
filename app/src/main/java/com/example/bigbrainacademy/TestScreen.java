package com.example.bigbrainacademy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bigbrainacademy.databinding.ActivityTestScreenBinding;

// view for the test part of the game
public class TestScreen extends AppCompatActivity implements ActivityInterface {
  private ActivityTestScreenBinding bind;
  private View view;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init_view();
    init_buttons();
    countdown_timer(3, findViewById(R.id.test_screen_timer));
  }

  private void countdown_timer(final int time, TextView text) {
    new CountDownTimer(time * 1000, 1000) {
      int t = time;
      @Override
      public void onTick(long l) {
        text.setText(String.valueOf(t));
        --t;
      }

      @Override
      public void onFinish() {
        text.setText(R.string.test_screen_countdown_end);
        startActivity(new Intent(TestScreen.this, WrittenMathScreen.class));
      }
    }.start();
  }

  @Override
  public void init_buttons() {

  }

  @Override
  public void init_view() {
    bind = ActivityTestScreenBinding.inflate(getLayoutInflater());
    view = bind.getRoot();
    setContentView(view);
  }
}