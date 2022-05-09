package com.example.bigbrainacademy;

import static com.example.bigbrainacademy.R.*;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bigbrainacademy.databinding.ActivityWrittenMathScreenBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WrittenMathScreen extends AppCompatActivity implements ActivityInterface, View.OnClickListener {
    private WrittenMath calcState;
    private ActivityWrittenMathScreenBinding bind;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Buttons must be initialized after the view, move all init_button calls to the end of init_view
        init_view();
        init_buttons();
    }

    @Override
    public void init_buttons() {
        Button zero = findViewById(id.calc_0);
        Button one = findViewById(id.calc_1);
        Button two = findViewById(id.calc_2);
        Button three = findViewById(id.calc_3);
        Button four = findViewById(id.calc_4);
        Button five = findViewById(id.calc_5);
        Button six = findViewById(id.calc_6);
        Button seven = findViewById(id.calc_7);
        Button eight = findViewById(id.calc_8);
        Button nine = findViewById(id.calc_9);
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
    public void init_view() {
        bind = ActivityWrittenMathScreenBinding.inflate(getLayoutInflater());
        view = bind.getRoot();
        setContentView(view);
        calcState = new WrittenMath();
    }

    @Override
    public void onClick(View v) {
        char digit = 0;
        switch (v.getId()) {
            case id.calc_0:
                digit = '0';
                break;
            case id.calc_1:
                digit = '1';
                break;
            case id.calc_2:
                digit = '2';
                break;
            case id.calc_3:
                digit = '3';
                break;
            case id.calc_4:
                digit = '4';
                break;
            case id.calc_5:
                digit = '5';
                break;
            case id.calc_6:
                digit = '6';
                break;
            case id.calc_7:
                digit  = '7';
                break;
            case id.calc_8:
                digit = '8';
                break;
            case id.calc_9:
                digit = '9';
                break;
        }
        calcState.addDigit(digit);
        update_answer();
        check_answer();

    }

    private void update_answer() {
        TextView text = view.findViewById(id.input_box_written_math_screen);
        text.setText(calcState.getInput());
    }

    private void check_answer() {
        if (calcState.isCorrect()) {
            // do correct answer stuff here
            view.setBackgroundColor(getResources().getColor(color.green, getTheme()));
        }
        else {
            // check if there should be more input
            if (!calcState.isContinue()) {
                // input was wrong
                view.setBackgroundColor(getResources().getColor(color.red, getTheme()));
            }
        }
    }
}