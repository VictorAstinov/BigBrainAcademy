package com.example.bigbrainacademy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import java.util.ArrayList;

public abstract class AbstractActivity extends AppCompatActivity {

    // attributes that could be used in controllers/views
    // TODO: look to replace any CountDownTimer that doesnt use OnTick with a Handler
    private ArrayList<CountDownTimer> countDownTimers = new ArrayList<>();

    // private methods for only the abstract base class

    private void endTimers() {
        for (CountDownTimer timer : countDownTimers) {
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    private void circularOutAnimation(View v) {
        final int xCoor = v.getWidth() / 2;
        final int yCoor = v.getHeight() / 2;
        final float radius = (float)Math.hypot(xCoor, yCoor);

        Animator animate = ViewAnimationUtils.createCircularReveal(v, xCoor, yCoor, radius, 0);

        animate.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.INVISIBLE);
            }
        });

        animate.start();
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
        // the only positive to this is that it asserts that the view is initialized before the buttons
        // which is required, Button initialization might also be a large function
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
    /*
    protected void startCountDownScreen (final int time, Runnable run) {
        // hide all buttons and find countdown view
        toggleScreenContents(false);

        TextView countdownTimer = getCountdownView();
        countdownTimer.setVisibility(View.VISIBLE);
        // make sure countdownTimer is visible
        // create countdown timer
        CountDownTimer timer = new CountDownTimer((time + 1) * 1000, 1000) {
            int t = time;
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 1000) { // last tick
                    countdownTimer.setText(R.string.test_screen_countdown_end);
                    return;
                }
                countdownTimer.setText(String.valueOf(t));
                --t;
            }

            @Override
            public void onFinish() {
                circularOutAnimation(countdownTimer);
                toggleScreenContents(true);
                run.run();
            }
        };
        add_timer(timer);
        timer.start();
    }

     */

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
    protected abstract void init_buttons(); // do we really need these to be forced, initialization can be done in the ctor call individually
    protected abstract void init_view(); // ^^^
    protected abstract void toggle_buttons(boolean enable); // might not be needed
    protected abstract Intent moveToResultsScreen(); // probably need

}
