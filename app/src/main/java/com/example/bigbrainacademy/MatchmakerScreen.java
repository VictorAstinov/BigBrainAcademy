package com.example.bigbrainacademy;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.bigbrainacademy.databinding.ActivityMatchmakerScreenBinding;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Objects;


// TODO: add the boxes that show the currently matched pairs, use gridLayout
public class MatchmakerScreen extends AbstractActivity implements RecyclerViewAdapter.ItemClickListener {

    private View mainView;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private final MatchmakerGame gameState = new MatchmakerGame();
    private PressedButton pressedButton = null;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private class PressedButton {
        public Integer value;
        public View button; // we have to store a reference to layout so that buttons can be disabled

        public PressedButton(Integer val, View view) {
            value = val;
            button = view;
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            // we only care about the logical equality of the buttons, TextView is stored so that
            // we have a reference to the first button pressed
            PressedButton that = (PressedButton) o;
            return Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, button);
        }
    }

    public static class CustomGridLayoutManager extends FlexboxLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context) {
            super(context);
            setJustifyContent(JustifyContent.CENTER);
            setFlexWrap(FlexWrap.WRAP);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        CustomGridLayoutManager layoutManager = new CustomGridLayoutManager(this);
        layoutManager.setScrollEnabled(false);

        recyclerView = findViewById(R.id.matchmaker_recycleView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        createAppRuntimeTimer(30, findViewById(R.id.matchmaker_timerbox));
        run();
    }

    private void createGrid(ArrayList<Integer> data, int cols) {
        // should probably resize the recyclerview to keep the squares the same size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // in theory this should be constant (the width of the button), and we resize the recyclerview
        // to width * cols
        adapter = new RecyclerViewAdapter(this, data, displayMetrics.widthPixels / cols);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void run() {
        ArrayList<Integer> temp = gameState.generateGrid();

        displayPairCount();
        createGrid(temp, gameState.getColumns());

        // enable touch in case it has been disabled
        toggle_buttons(true);
    }

    // make the pair count visible for 1 second before showing the recyclerview
    private void displayPairCount() {

        recyclerView.setVisibility(View.GONE);

        TextView pairCount = findViewById(R.id.matchmaker_pattern_count);
        int pairs = gameState.getPairCount();
        String str = pairs == 1 ? getString(R.string.matchmaker_screen_pair, pairs) : getString(R.string.matchmaker_screen_pairs, pairs);

        pairCount.setText(str);
        pairCount.setVisibility(View.VISIBLE);

        handler.postDelayed(() -> {pairCount.setVisibility(View.GONE); recyclerView.setVisibility(View.VISIBLE);}, 1000);
    }

    @Override
    protected void init_view() {
        ActivityMatchmakerScreenBinding bind = ActivityMatchmakerScreenBinding.inflate(getLayoutInflater());
        mainView = bind.getRoot();
        setContentView(mainView);
    }

    @Override
    protected void init_buttons() {

    }

    // works but is brute force, disables all touch events on the entire screen. Should probably add
    // functionality to the adapter
    @Override
    protected void toggle_buttons(boolean enable) {
        // recyclerView.setEnabled(enable);
        if (!enable) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    protected Intent moveToResultsScreen() {
        finish();
        Intent intent = new Intent(this, ResultsScreen.class);
        // TODO: impl
        return intent;
    }

    // if 2 buttons have been pressed, check if pressedButtons value == pressedValue
    // pressedValue comes from the second button that is pressed
    private void calculateMatchMade(PressedButton pressedValue) {

        int color;
        boolean match;
        // match has been made, disable buttons that were matched
        if (pressedValue.equals(pressedButton)) {
            color = ContextCompat.getColor(this, R.color.MediumSpringGreen);
            match = true;

            pressedButton.button.setEnabled(false);
            pressedValue.button.setEnabled(false);

            gameState.pairMade();
        }
        else {
            color = ContextCompat.getColor(this, R.color.DarkRed);
            match = false;
        }

        // change background colors of buttons + unset the pressedButton reference
        getTextView(pressedButton.button).setBackgroundColor(color);
        getTextView(pressedValue.button).setBackgroundColor(color);

        pressedButton = null;
        handlePairPressed(match);
    }

    // handles the result when a pair has been pressed. Must check if all pairs have been made,
    // if matchMade is false question is wrong and we restart the process
    private void handlePairPressed(boolean matchMade) {

        // still input to be had, we simply return here
        if (matchMade && gameState.getPairCount() != gameState.getCurrentPairs()) {
            return;
        }
        // match was made and it was the final match needed, or it was not made and the answer is wrong
        else {
            resetScreen(matchMade);
        }

    }

    private void resetScreen(boolean wasRight) {

        gameState.calcScore(wasRight);

        toggle_buttons(false);
        int color = wasRight ? R.color.LightGreen : R.color.holo_red_light;

        changeScreenColor(mainView, this, 250, R.color.Lavender, color);
        handler.postDelayed(this::run, 250);
    }

    // Each view is a linear layout with a textView child. View must be cast as a ViewGroup to
    // get the get at the first (and only) child.
    private TextView getTextView(View view) {
        return (TextView) ((ViewGroup)view).getChildAt(0);
    }

    @Override
    public void onItemClick(View view, int position) {
        // TODO: impl
        /*
        Context c = this;
        view.setBackgroundColor(ContextCompat.getColor(c, R.color.DarkOrchid));
        new CountDownTimer(200, 200) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                view.setBackgroundColor(ContextCompat.getColor(c, R.color.SkyBlue));
            }
        }.start(); */

        TextView button = getTextView(view);
        ColorDrawable buttonBackground = (ColorDrawable) button.getBackground();

        // pressed button, set attribute to refer to this button
        if (buttonBackground.getColor() == ContextCompat.getColor(this, R.color.MediumAquamarine)) {

            button.setBackgroundColor(ContextCompat.getColor(this, R.color.DarkOrange));

            // check if another button has been pressed, if so we need to check if a match has been made
            // If a button has not been pressed, we set the pressed reference to this button
            PressedButton pressed = new PressedButton(adapter.getItemValue(position), view);
            if (pressedButton == null) {
                pressedButton = pressed;
            }
            else {
                calculateMatchMade(pressed);
            }
        }
        // unset reference
        else {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.MediumAquamarine));
            pressedButton = null;
        }

    }
}