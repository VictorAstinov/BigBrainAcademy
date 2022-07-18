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

import java.math.MathContext;

// view for the test part of the game
// TODO: This should prob be renamed to countdownScreen or something like that
public class TestScreen extends AppCompatActivity implements ActivityInterface {

    private View view;
    private CountDownTimer timer = null;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        init_view();
        init_buttons();
        startCountdown(3500, findViewById(R.id.test_screen_timer));
    }

    private void startCountdown(final int timeinMS, TextView text) {
        timer = new CountDownTimer(timeinMS, 1000) {
            int t = timeinMS / 1000;

            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 1000) { // last tick
                    text.setText(R.string.test_screen_countdown_end);
                    return;
                }
                text.setText(String.valueOf(t));
                --t;
            }

            @Override
            public void onFinish() {
                // modify to launch desired activity
                ActivityScreenEnum activity = (ActivityScreenEnum) intent.getSerializableExtra("ACTIVITY_NAME");
                if (activity != null) {
                    // launch desired activity, currently used for restarting an activity, this should prob be generalized for all activities
                    startActivity(new Intent(TestScreen.this, ResultsScreen.getPreviousScreen(activity)));
                }
                // this should probably throw an error, this class should only be called when we know what game will be run
                // for now we can use it for testing
                else {
                    startActivity(new Intent(TestScreen.this, MatchmakerScreen.class));
                }
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
                finish();
            }
        };
        timer.start();
    }


    @Override
    public void init_buttons() {

    }

    @Override
    public void init_view() {
        ActivityTestScreenBinding bind = ActivityTestScreenBinding.inflate(getLayoutInflater());
        view = bind.getRoot();
        setContentView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        endTimer();
    }

    private void endTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}