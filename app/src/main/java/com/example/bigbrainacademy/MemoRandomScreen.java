package com.example.bigbrainacademy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigbrainacademy.databinding.ActivityMemoRandomScreenBinding;

import java.util.ArrayList;

public class MemoRandomScreen extends AppCompatActivity implements ActivityInterface, View.OnClickListener {

    private MemoRandomGame memoRandomState;
    private ActivityMemoRandomScreenBinding bind;
    private View view;

    private ArrayList<LinearLayout> answerRowLayouts;
    private Button memorizedButton;

    private final String LAYOUT_ID = "memorandom_answer_row_";
    private final String ANSWER_BUTTON_ID = "memorandom_answer_button_";
    private final String ICON_ID = "drawable/memorandom_icon_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_view();
        init_buttons();
    }

    @Override
    public void init_buttons() {
        int rowIndex = 0;
        for (LinearLayout row : answerRowLayouts) {
            int rowLength = memoRandomState.getRowLength(rowIndex++);
            for (int i = 0; i < rowLength; ++i) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Drawable icon = getMatchingIcon(i);

                Button button = new Button(this);
                button.setBackground(icon);

                String buttonIDName = ANSWER_BUTTON_ID + Integer.toString(i);
                int buttonResId = getResources().getIdentifier(buttonIDName, "id", getPackageName());
                button.setId(buttonResId);

                button.setOnClickListener(this);

                row.addView(button, params);
            }
        }
        memorizedButton = findViewById(R.id.memorized_button);
        memorizedButton.setOnClickListener(this);
    }

    @Override
    public void init_view() {
        bind = ActivityMemoRandomScreenBinding.inflate(getLayoutInflater());
        view = bind.getRoot();
        setContentView(view);
        memoRandomState = new MemoRandomGame();

        setAnswerGrid();
    }

    public void setAnswerGrid() {

        LinearLayout answersLayout = findViewById(R.id.layout_answers);
        int height = memoRandomState.getHeight();
        ArrayList<Integer> row;
        answerRowLayouts = new ArrayList<>();

        for (int i = 0; i < height; ++i) {

            LinearLayout rowLayout = new LinearLayout(this);

            String idName = LAYOUT_ID + Integer.toString(i);
            int resId = getResources().getIdentifier(idName,"id", getPackageName());
            rowLayout.setId(resId);

            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rowLayout.setLayoutParams(params);

            answersLayout.addView(rowLayout);
            answerRowLayouts.add(rowLayout);
        }
    }

    public Drawable getMatchingIcon(int index) {
        String idName = ICON_ID + Integer.toString(index);
        int resID = getResources().getIdentifier(idName,"id", getPackageName());
        Drawable icon = ResourcesCompat.getDrawable(getResources(), resID, null);
        return icon;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.memorized_button: startRound();
        }
    }

    private void startRound() {
        memorizedButton.setVisibility(View.GONE);
        for (Coordinate hiddenCoordinate : memoRandomState.getHiddenCoordinates()) {
            int x = hiddenCoordinate.getRow();
            int y = hiddenCoordinate.getColumn();
            //TODO figure out why this fails when the other ones don't
            @SuppressLint("ResourceType") Drawable hiddenIcon = ResourcesCompat.getDrawable(getResources(), R.id.memorandom_hidden_icon, null);
            answerRowLayouts.get(x).getChildAt(y).setBackground(hiddenIcon);
        }
    }
}