package com.example.bigbrainacademy;

import static com.example.bigbrainacademy.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bigbrainacademy.databinding.ActivityWrittenMathScreenBinding;

public class WrittenMathScreen extends AbstractActivity implements View.OnClickListener {
    private WrittenMath calcState;
    private ActivityWrittenMathScreenBinding bind;
    private View view;
    private CountDownTimer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Buttons must be initialized after the view, move all init_button calls to the end of init_view
        init();
        createAppRuntimeTimer(60, findViewById(id.timer_box_written_math));
    }

    @Override
    protected void init_buttons() {
        Button zero = findViewById(id.calc_pad_0);
        Button one = findViewById(id.calc_pad_1);
        Button two = findViewById(id.calc_pad_2);
        Button three = findViewById(id.calc_pad_3);
        Button four = findViewById(id.calc_pad_4);
        Button five = findViewById(id.calc_pad_5);
        Button six = findViewById(id.calc_pad_6);
        Button seven = findViewById(id.calc_pad_7);
        Button eight = findViewById(id.calc_pad_8);
        Button nine = findViewById(id.calc_pad_9);
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
    }


    @Override
    protected void init_view() {
        bind = ActivityWrittenMathScreenBinding.inflate(getLayoutInflater());
        view = bind.getRoot();
        setContentView(view);
        calcState = new WrittenMath(this);
        TextView t = findViewById(id.prob_box_written_math_screen);
        t.setText(calcState.getProblem());
    }

    @Override
    public void onClick(View v) {
        char digit = 0;
        switch (v.getId()) {
            case id.calc_pad_0:
                digit = '0';
                break;
            case id.calc_pad_1:
                digit = '1';
                break;
            case id.calc_pad_2:
                digit = '2';
                break;
            case id.calc_pad_3:
                digit = '3';
                break;
            case id.calc_pad_4:
                digit = '4';
                break;
            case id.calc_pad_5:
                digit = '5';
                break;
            case id.calc_pad_6:
                digit = '6';
                break;
            case id.calc_pad_7:
                digit  = '7';
                break;
            case id.calc_pad_8:
                digit = '8';
                break;
            case id.calc_pad_9:
                digit = '9';
                break;
        }
        calcState.addDigit(digit);
        update_answer();
        check_answer();

    }

    private void update_answer() {
        TextView text = view.findViewById(id.input_box_written_math);
        text.setText(calcState.getInput());
    }

    private void check_answer() {
        if (calcState.isCorrect()) {
            // do correct answer stuff here
            next_question(true);
        }
        else {
            // check if there should be more input
            if (!calcState.isContinue()) {
                // input was wrong
                next_question(false);
            }
        }
    }

    // used to enable (true) or disable (false) buttons.
    protected void toggle_buttons(boolean enable) {
        Button zero = findViewById(id.calc_pad_0);
        Button one = findViewById(id.calc_pad_1);
        Button two = findViewById(id.calc_pad_2);
        Button three = findViewById(id.calc_pad_3);
        Button four = findViewById(id.calc_pad_4);
        Button five = findViewById(id.calc_pad_5);
        Button six = findViewById(id.calc_pad_6);
        Button seven = findViewById(id.calc_pad_7);
        Button eight = findViewById(id.calc_pad_8);
        Button nine = findViewById(id.calc_pad_9);
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



    private void next_question(boolean wasRight) {
        reset_screen(wasRight);
        calcState.setInput("");
        final Handler handler = new Handler(Looper.getMainLooper());
        // this needs to call a function to clear input and generate the problem
        handler.postDelayed(this::clear_screen, 250);
        // TextView prob = findViewById(id.prob_box_written_math_screen);
        // prob.setText(calcState.generateProblem());
    }

    private void clear_screen() {
        TextView prob = findViewById(id.prob_box_written_math_screen);
        TextView input = findViewById(id.input_box_written_math);
        prob.setText(calcState.generateProblem());
        input.setText("");

    }

    private void reset_screen(boolean wasRight) {
        calcState.setInput("");
        if (wasRight) {
            changeScreenColor(view, this, 250, R.color.CadetBlue, color.green);
            // view.setBackgroundColor(getResources().getColor(color.green, getTheme()));
        }
        else {
            changeScreenColor(view, this, 250, R.color.CadetBlue, color.red);
            // view.setBackgroundColor(getResources().getColor(color.red, getTheme()));
        }
    }

    @Override
    protected Intent moveToResultsScreen() {
        finish();
        Intent intent = new Intent(this, ResultsScreen.class);
        intent.putExtra("TOTAL_SCORE", calcState.getScore());
        intent.putExtra("TOTAL_CORRECT", calcState.getTotalRight());
        intent.putExtra("TOTAL_WRONG", calcState.getTotalWrong());
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}