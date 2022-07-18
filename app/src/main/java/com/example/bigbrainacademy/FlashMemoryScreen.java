package com.example.bigbrainacademy;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bigbrainacademy.databinding.ActivityFlashMemoryScreenBinding;
import com.ibm.icu.text.RuleBasedNumberFormat;

import java.util.Locale;

public class FlashMemoryScreen extends AbstractActivity implements View.OnClickListener {

    private View view;
    private FlashMemory flashState;
    private boolean handlerEnabled = false;
    private final Handler.Callback callback = msg -> {
        handlerEnabled = false;
        return false;
    };
    private final Handler handle = new Handler(Looper.getMainLooper(), callback);


    private void addPostDelayed(Runnable runnable, final int timeInMS) {
        handlerEnabled = true;
        handle.postDelayed(runnable, timeInMS);
    }

    private final Runnable clearTextRunnable = () -> {TextView text = findViewById(R.id.input_box_flash_memory); text.setText("");};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init view + buttons
        flashState = new FlashMemory();
        init();
        // start countdown screen, launch game after timer expires
        // startCountDownScreen(3, () -> run());
        run();
    }

    private void run(){
        // start game and toggle buttons to false to avoid input before answers shows on screen
        createAppRuntimeTimer(30, findViewById(R.id.timer_box_flash_memory));
        toggle_buttons(false);
        showAnswerLen(flashState.getTimeInterval(), flashState.generateProblem());
    }
    @Override
    public void init_view() {
        ActivityFlashMemoryScreenBinding bind = ActivityFlashMemoryScreenBinding.inflate(getLayoutInflater());
        view = bind.getRoot();
        setContentView(view);
    }

    @Override
    public void init_buttons() {
        // inits all buttons
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
        // cancel clearing text if handler is active
        if  (handlerEnabled) {
            handle.removeCallbacks(clearTextRunnable);
            handlerEnabled = false;
        }
        // add digit and update answer
        flashState.addDigit(digit);
        updateAnswer();
        checkAnswer();
    }

    private void showAnswerLen(final int time, String answer) {
        TextView text = findViewById(R.id.input_box_flash_memory);
        text.setText(convertIntoWords(answer.length()));
        addPostDelayed(() -> showAnswer(text, answer, time), 1000);
        // handle.postDelayed(() -> showAnswer(text, answer, time), 1000);
    }

    private void showAnswer(TextView text, String ans, final int time) {
        // allow for user input and show answer
        toggle_buttons(true);
        text.setText(ans);
        // clear text after given time has elapsed
        addPostDelayed(clearTextRunnable, time);
        // handle.postDelayed(() -> text.setText(""), time);
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
        toggle_buttons(false);

        TextView special_button = findViewById(R.id.flash_pad_special);
        special_button.setText(String.valueOf(flashState.getSpecial()));

        ConstraintLayout layout = findViewById(R.id.flash_memory_layout);
        ColorDrawable backgroundColor = (ColorDrawable) layout.getBackground();


        if (wasRight) {
            changeScreenColor(view, this, 250, R.color.LightSteelBlue, R.color.LightGreen);
            // view.setBackgroundColor(getResources().getColor(R.color.green, getTheme()));
        }
        else {
            changeScreenColor(view, this, 250, R.color.LightSteelBlue, R.color.holo_red_light);
        }
        // toggle_buttons(false);

    }

    private void next_question(boolean wasRight) {
        reset_screen(wasRight);
        addPostDelayed(() -> showAnswerLen(flashState.getTimeInterval(), flashState.generateProblem()), 250);
        // handle.postDelayed(() -> showAnswerLen(flashState.getTimeInterval(), flashState.generateProblem()), 250);
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
        Button special = findViewById(R.id.flash_pad_special);
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
        special.setEnabled(enable);
    }
    /*
    @Override
    protected void toggleScreenContents(boolean areOn) {

        int visibility = areOn ? View.VISIBLE : View.GONE;
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flash_memory_layout);
        int viewCount = viewGroup.getChildCount();

        for (int i = 0; i < viewCount; ++i) {
            View v = viewGroup.getChildAt(i);
            v.setVisibility(visibility);
        }
        View countdownTimer = findViewById(R.id.flash_starting_countdown);
        if (areOn) {
            countdownTimer.setVisibility(View.GONE);
        }
        else {
            countdownTimer.setVisibility(View.VISIBLE);
        }
        ConstraintLayout layout = findViewById(R.id.flash_memory_layout);
        if (areOn) {
            layout.setVisibility(View.VISIBLE);
        }
        else {
            layout.setVisibility(View.GONE);
        }
    }
    */


    @Override
    protected Intent moveToResultsScreen() {
        finish();
        Intent intent = new Intent(this, ResultsScreen.class);
        intent.putExtra("TOTAL_SCORE", flashState.getScore()); // these strings should be constants and wrapped in another class
        intent.putExtra("TOTAL_CORRECT", flashState.getTotalRight());
        intent.putExtra("TOTAL_WRONG", flashState.getTotalWrong());
        intent.putExtra("PREV_SCREEN", ActivityScreenEnum.FlashMemoryScreen);
        return intent;
    }

    @Override
    public void onBackPressed() {
        // disabled, maybe implement a way to make sure that game isnt restarted differently
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}