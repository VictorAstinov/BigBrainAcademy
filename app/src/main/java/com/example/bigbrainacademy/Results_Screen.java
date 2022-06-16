package com.example.bigbrainacademy;

import static com.example.bigbrainacademy.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bigbrainacademy.databinding.ActivityResultsScreenBinding;

public class Results_Screen extends AppCompatActivity implements ActivityInterface, View.OnClickListener {

    private ActivityResultsScreenBinding bind;
    private View view;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_screen);
        intent = getIntent();
        init_view();
        init_buttons();
    }

    @Override
    public void init_view() {
        bind = ActivityResultsScreenBinding.inflate(getLayoutInflater());
        view = bind.getRoot();
        setContentView(view);

    }

    @Override
    public void init_buttons() {
       Button cont = findViewById(id.result_screen_continue);
       Button tryAgain = findViewById(id.result_screen_tryAgain);

       cont.setOnClickListener(this);
       tryAgain.setOnClickListener(this);

       String score = String.valueOf(intent.getIntExtra("TOTAL_SCORE", 0));
       int totalCorrect = intent.getIntExtra("TOTAL_CORRECT", 0);
       int totalWrong = intent.getIntExtra("TOTAL_WRONG", 0);
       TextView scoreText = findViewById(id.result_screen_score);
       TextView correctText = findViewById(id.result_screen_total_correct);
       TextView wrongText = findViewById(id.result_screen_total_wrong);
       TextView percentText = findViewById(id.result_screen_percent_correct);
       int percent = Math.round((totalCorrect * 100f) / (totalCorrect + totalWrong));

       scoreText.setText(score);
       correctText.setText(getString(string.results_screen_correct, totalCorrect));
       wrongText.setText(getString(string.results_screen_missed, totalWrong));
       percentText.setText(getString(string.results_screen_percent, percent));
    }

    @Override
    public void onClick(View view) {
        ActivityScreenEnum prevActivity = (ActivityScreenEnum)intent.getSerializableExtra("PREV_SCREEN");
        switch (view.getId()) {
            case id.result_screen_tryAgain:
                startActivity(new Intent(this, getPreviousScreen(prevActivity)));
                break;
            case id.result_screen_continue:
                break;
        }
    }

    public static final Class getPreviousScreen(ActivityScreenEnum prev) {
        Class ret_val;
        switch (prev) {
            case ChoiceScreen:
                ret_val = ChoiceScreen.class;
                break;
            case ResultsScreen:
                ret_val = Results_Screen.class;
                break;
            case FlashMemoryScreen:
                ret_val = FlashMemoryScreen.class;
                break;
            case WrittenMathScreen:
                ret_val = WrittenMathScreen.class;
                break;
            case MemoRandomScreen:
                ret_val = MemoRandomScreen.class;
                break;
             // in case an invalid enum was received, go back to main screen
            default:
                ret_val = MainActivity.class;
        }
        return ret_val;
    }

}