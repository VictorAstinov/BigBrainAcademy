package com.example.bigbrainacademy;


import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bigbrainacademy.databinding.ActivityAnimalLinesScreenBinding;

import java.util.ArrayList;

// TODO: use a recyclerview with the CustomGridLayout to implement the grid
// TODO: each box needs to have the constraining dimensions (top/bottom, left/right)
// TODO: Override onTouch method detect the start/end points of the line drawn
// TODO: if line start and end are within start/end boxes, correct answer
// TODO: when line is being drawn check which boxes it goes through and highlight them
public class AnimalLinesScreen extends AbstractActivity implements View.OnTouchListener {

    private View mainView;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        recyclerView = findViewById(R.id.animalLines_recyclerView);
        MatchmakerScreen.CustomGridLayoutManager layoutManager= new MatchmakerScreen.CustomGridLayoutManager(this);
        layoutManager.setScrollEnabled(false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnTouchListener(this);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        ArrayList<Integer> data = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            data.add(i);
        }

        // in theory this should be constant (the width of the button), and we resize the recyclerview
        // to width * cols
        adapter = new RecyclerViewAdapter(this, data, displayMetrics.widthPixels / 5);
        // adapter.setTouchListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void toggle_buttons(boolean enable) {

    }

    @Override
    protected void init_buttons() {

    }

    @Override
    protected void init_view() {

        ActivityAnimalLinesScreenBinding bind = ActivityAnimalLinesScreenBinding.inflate(getLayoutInflater());
        mainView = bind.getRoot();
        setContentView(mainView);

    }

    @Override
    protected Intent moveToResultsScreen() {
        return null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();

        // poll the current coordinates when there is a touch on the screen, use as a pivot
        if (action== MotionEvent.ACTION_DOWN) {

        }
        // change the endpoint of the drawn line
        else if (action == MotionEvent.ACTION_MOVE) {

        }
        // when the touch is released, calculate if the line was in the correct range
        // if the line is within the starting box,
        else if (action == MotionEvent.ACTION_UP) {

        }
        return true;
    }
}