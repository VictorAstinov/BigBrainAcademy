package com.example.bigbrainacademy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

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

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    // methods that must be defined by the view/controllers
    protected abstract void init_buttons();
    protected abstract void init_view();
    protected abstract void toggle_buttons(boolean enable); // might not be needed
    // protected abstract void toggleScreenContents(boolean areOn); // not needed
    protected abstract Intent moveToResultsScreen();
    // protected abstract TextView getCountdownView(); // not needed

}
