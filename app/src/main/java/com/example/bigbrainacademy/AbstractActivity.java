package com.example.bigbrainacademy;

import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public abstract class AbstractActivity extends AppCompatActivity {

    // attributes that could be used in controllers/views
    protected ArrayList<CountDownTimer> countDownTimers = new ArrayList<>();

    private void endTimers() {
        for (CountDownTimer timer : countDownTimers) {
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    protected void createCountdownTimer(final int time, TextView text) {
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

            }
        };
        add_timer(timer);
        timer.start();
    }
    // methods that could be commonly used in views/controllers
    protected void add_timer(CountDownTimer timer) {
        countDownTimers.add(timer);
    }

    protected void init() {
        init_view();
        init_buttons();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        endTimers();
    }
    // methods that must be defined by the view/controllers
    protected abstract void init_buttons();
    protected abstract void init_view();
    protected abstract void toggle_buttons(boolean enable);
}
