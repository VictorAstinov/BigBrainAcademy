package com.example.bigbrainacademy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bigbrainacademy.databinding.ActivityFlashMemoryScreenBinding;
import com.ibm.icu.text.RuleBasedNumberFormat;

import java.util.Locale;

public class FlashMemoryScreen extends AbstractActivity implements View.OnClickListener {
    private View view;
    private FlashMemory flashState;
    private Handler handle = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        startCountDownScreen();
        handle.postDelayed(() -> run(), 3000);
    }

    private void run(){
        createAppRuntimeTimer(10, findViewById(R.id.timer_box_flash_memory));
        showAnswerLen(flashState.getTimeInterval(), flashState.generateProblem());
    }
    @Override
    public void init_view() {
        ActivityFlashMemoryScreenBinding bind = ActivityFlashMemoryScreenBinding.inflate(getLayoutInflater());
        view = bind.getRoot();
        setContentView(view);
        flashState = new FlashMemory();
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
        toggle_buttons(false);
        handle.postDelayed(() -> showAnswer(text, answer, time), 1000);
    }

    private void showAnswer(TextView text, String ans, final int time) {
        toggle_buttons(true);
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
            changeScreenColor(view, this, 250, R.color.CadetBlue, R.color.green);
            // view.setBackgroundColor(getResources().getColor(R.color.green, getTheme()));
        }
        else {
            changeScreenColor(view, this, 250, R.color.CadetBlue, R.color.red);
        }
        // toggle_buttons(false);

    }

    private void next_question(boolean wasRight) {
        reset_screen(wasRight);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> showAnswerLen(flashState.getTimeInterval(), flashState.generateProblem()), 250);
        // showAnswerLen(flashState.getTimeInterval(), flashState.generateProblem());
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
    @Override
    protected void toggle_buttons(boolean enable) {
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
        zero.setEnabled(enable);
        one.setEnabled(enable);
        two.setEnabled(enable);
        three.setEnabled(enable);
        four.setEnabled(enable);
        five.setEnabled(enable);
        six.setEnabled(enable);
        seven.setEnabled(enable);
        eight.setEnabled(enable);
        nine.setEnabled(enable);
    }

    @Override
    protected void toggleScreenContents(boolean areOn) {
        ConstraintLayout layout = findViewById(R.id.flash_memory_layout);
        if (areOn) {
            layout.setVisibility(View.VISIBLE);
        }
        else {
            layout.setVisibility(View.GONE);
        }
    }

    @Override
    protected Intent moveToResultsScreen() {
        finish();
        Intent intent = new Intent(this, Results_Screen.class);
        intent.putExtra("TOTAL_SCORE", flashState.getScore());
        intent.putExtra("TOTAL_CORRECT", flashState.getTotalRight());
        intent.putExtra("TOTAL_WRONG", flashState.getTotalWrong());
        intent.putExtra("PREV_SCREEN", ActivityScreenEnum.FlashMemoryScreen);
        return intent;
    }

    @Override
    public void onBackPressed() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}