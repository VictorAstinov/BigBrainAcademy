package com.example.bigbrainacademy;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bigbrainacademy.databinding.ActivityFlashMemoryScreenBinding;
import com.ibm.icu.text.RuleBasedNumberFormat;

import java.util.Locale;

public class FlashMemoryScreen extends AbstractActivity implements View.OnClickListener {
    private ActivityFlashMemoryScreenBinding bind;
    private View view;
    private FlashMemory flashState;
    CountDownTimer timer = null;
    private Handler handle = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        createCountdownTimer(60, findViewById(R.id.timer_box_flash_memory));
    }

    @Override
    public void init_view() {
        bind = ActivityFlashMemoryScreenBinding.inflate(getLayoutInflater());
        view = bind.getRoot();
        setContentView(view);
        flashState = new FlashMemory();
        showAnswerLen(flashState.getTimeInterval(), flashState.generateProblem());
    }

    @Override
    public void init_buttons() {
        Button zero = findViewById(R.id.flash_pad_0);
        Button one = findViewById(R.id.flash_pad_1);
        Button two = findViewById(R.id.flash_pad_2);
        Button three = findViewById(R.id.flash_pad_3);
        Button four = findViewById(R.id.flash_pad_4);
        Button five = findViewById(R.id.flash_pad_5);
        Button six = findViewById(R.id.flash_pad_6);
        Button seven = findViewById(R.id.flash_pad_7);
        Button eight = findViewById(R.id.flash_pad_8);
        Button nine = findViewById(R.id.flash_pad_9);
        Button special = findViewById(R.id.flash_pad_special);
        zero.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        special.setOnClickListener(this);
        special.setText(String.valueOf(flashState.getSpecial()));
    }

    @Override
    public void onClick(View v) {
        char digit = 0;
        switch (v.getId()) {
            case R.id.flash_pad_0:
                digit = '0';
                break;
            case R.id.flash_pad_1:
                digit = '1';
                break;
            case R.id.flash_pad_2:
                digit = '2';
                break;
            case R.id.flash_pad_3:
                digit = '3';
                break;
            case R.id.flash_pad_4:
                digit = '4';
                break;
            case R.id.flash_pad_5:
                digit = '5';
                break;
            case R.id.flash_pad_6:
                digit = '6';
                break;
            case R.id.flash_pad_7:
                digit = '7';
                break;
            case R.id.flash_pad_8:
                digit = '8';
                break;
            case R.id.flash_pad_9:
                digit = '9';
                break;
            case R.id.flash_pad_special:
                digit = flashState.getSpecial();
                break;
        }
        flashState.addDigit(digit);
        updateAnswer();
        checkAnswer();
    }

    private void showAnswerLen(final int time, String answer) {
        TextView text = findViewById(R.id.input_box_flash_memory);
        text.setText(convertIntoWords(answer.length()));
        handle.postDelayed(() -> showAnswer(text, answer, time), 1000);
    }

    private void showAnswer(TextView text, String ans, final int time) {
        text.setText(ans);
        handle.postDelayed(() -> text.setText(""), time);
    }


    private String convertIntoWords(int str) {
        Locale local = new Locale(Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
        RuleBasedNumberFormat ruleBasedNumberFormat = new RuleBasedNumberFormat(local, RuleBasedNumberFormat.SPELLOUT);
        return ruleBasedNumberFormat.format(str);
    }

    private void updateAnswer() {
        TextView text = findViewById(R.id.input_box_flash_memory);
        // text.setText(formatInput(flashState.getInput()));
        text.setText(flashState.getInput());
    }

    private void reset_screen(boolean wasRight) {
        // TextView text = findViewById(R.id.input_box_flash_memory);
        // text.setText("");
        flashState.setInput("");

        TextView special_button = findViewById(R.id.flash_pad_special);
        special_button.setText(String.valueOf(flashState.getSpecial()));
        if (wasRight) {
            view.setBackgroundColor(getResources().getColor(R.color.green, getTheme()));
        }
        else {
            view.setBackgroundColor(getResources().getColor(R.color.red, getTheme()));
        }
        toggle_buttons(false);
        new CountDownTimer(250, 250) {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                view.setBackgroundColor(getResources().getColor(R.color.CadetBlue, getTheme()));
                // toggle_buttons(true);
                cancel();
            }
        }.start();
    }

    private void next_question(boolean wasRight) {
        reset_screen(wasRight);
        showAnswerLen(flashState.getTimeInterval(), flashState.generateProblem());
    }

    private void checkAnswer() {
        if (flashState.isCorrect()) {
            // correct answer
            next_question(true);
        }
        else {
            if (!flashState.isContinue()) {
                // wrong answer
                next_question(false);
            }
        }
    }

    protected void toggle_buttons(boolean enable) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}