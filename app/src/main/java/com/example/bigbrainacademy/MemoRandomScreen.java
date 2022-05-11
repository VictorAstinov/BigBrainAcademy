package com.example.bigbrainacademy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.bigbrainacademy.databinding.ActivityMemoRandomScreenBinding;

import java.util.ArrayList;

public class MemoRandomScreen extends AppCompatActivity implements ActivityInterface, View.OnClickListener {

    private MemoRandomGame memoRandomState;
    private ActivityMemoRandomScreenBinding bind;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_view();
        init_buttons();
    }

    @Override
    public void init_buttons() {

    }

    @Override
    public void init_view() {
        bind = ActivityMemoRandomScreenBinding.inflate(getLayoutInflater());
        view = bind.getRoot();
        setContentView(view);
        memoRandomState = new MemoRandomGame();

        setAnswerGrid();
    }

    @Override
    public void onClick(View view) {

    }

    public void setAnswerGrid() {

        LinearLayout answersLayout = findViewById(R.id.layout_answers);
        int height = memoRandomState.getHeight();
        ArrayList<Integer> row;

        for (int i = 0; i < height; ++i) {
            LinearLayout rowLayout = new LinearLayout(this);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            rowLayout.setLayoutParams(llp);

            row = memoRandomState.getRow(i);
            for (int iconIndex : row) {
                View icon = getMatchingIcon(iconIndex);
                rowLayout.addView(icon);
            }
        }
    }

    public View getMatchingIcon(int index) {
        // TODO: make this better
        String idName = "memorandom_icon_" + Integer.toString(index);
        int resID = getResources().getIdentifier(idName,"id", getPackageName());
        return findViewById(resID);
    }

}