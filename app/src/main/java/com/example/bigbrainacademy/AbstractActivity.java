package com.example.bigbrainacademy;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class AbstractActivity extends AppCompatActivity {

    // attributes that could be used in controllers/views
    protected ArrayList<CountDownTimer> countDownTimers = new ArrayList<>();

    // private methods for only the abstract base class
    private void endTimers() {
        for (CountDownTimer timer : countDownTimers) {
            if (timer != null) {
                timer.cancel();
            }
        }
    }
    // methods that could be commonly used in views/controllers
    protected void createAppRuntimeTimer(final int time, TextView text) {
        Context self = this;
        CountDownTimer timer = new CountDownTimer(1000 * time, 1000) {
            int t = time;
            @Override
            public void onTick(long l) {
                text.setText(String.valueOf(t));
                --t;
            }

            @Override
            public void onFinish() {
                text.setText(R.string.end_timer_string);
                toggle_buttons(false);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> startActivity(moveToResultsScreen()), 1000);
            }
        };
        add_timer(timer);
        timer.start();
    }
    protected void add_timer(CountDownTimer timer) {
        countDownTimers.add(timer);
    }

    protected void init() {
        init_view();
        init_buttons();
    }

    protected void changeScreenColor(View view, Context context, final int timeInMS, int initColor, int tempColor) {
        view.setBackgroundColor(ContextCompat.getColor(context, tempColor));
        CountDownTimer timer = new CountDownTimer(timeInMS, timeInMS) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                view.setBackgroundColor(ContextCompat.getColor(context, initColor));
            }
        };
        add_timer(timer);
        timer.start();
    }

    /*
    protected void disableButtons(final int timeinMS, Consumer function) {
        function.accept(false);
        CountDownTimer timer = new CountDownTimer(timeinMS, timeinMS) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                function.accept(true);
            }
        };
        add_timer(timer);
        timer.start();
    } */

    protected void startCountDownScreen () {
        // hide all buttons
        toggleScreenContents(false);

        // create countdown timer
        final int time = 3;
        CountDownTimer timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                toggleScreenContents(true);
            }
        };
        add_timer(timer);
        timer.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        endTimers();
        super.onDestroy();
    }


    // methods that must be defined by the view/controllers
    protected abstract void init_buttons();
    protected abstract void init_view();
    protected abstract void toggle_buttons(boolean enable);
    protected abstract void toggleScreenContents(boolean areOn);
    protected abstract Intent moveToResultsScreen();

}
