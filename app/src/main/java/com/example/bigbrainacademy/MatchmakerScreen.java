package com.example.bigbrainacademy;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.bigbrainacademy.databinding.ActivityMatchmakerScreenBinding;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

public class MatchmakerScreen extends AbstractActivity implements RecyclerViewAdapter.ItemClickListener {

    private View view;
    private RecyclerViewAdapter adapter;

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

        ArrayList<Integer> data = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {  // this should be an actual limit determined by the gameState
            data.add(i);
        }

        CustomGridLayoutManager layoutManager = new CustomGridLayoutManager(this);
        layoutManager.setScrollEnabled(false);


        RecyclerView recyclerView = findViewById(R.id.matchmaker_recycleView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        adapter = new RecyclerViewAdapter(this, data, displayMetrics.widthPixels / 5); // this should be (number above / rows wanted)
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void init_view() {
        ActivityMatchmakerScreenBinding bind = ActivityMatchmakerScreenBinding.inflate(getLayoutInflater());
        view = bind.getRoot();
        setContentView(view);
    }

    @Override
    protected void init_buttons() {

    }

    @Override
    protected void toggle_buttons(boolean enable) {

    }

    @Override
    protected Intent moveToResultsScreen() {
        finish();
        Intent intent = new Intent(this, ResultsScreen.class);
        // TODO: impl
        return intent;
    }

    @Override
    public void onItemClick(View view, int position) {
        // TODO: impl
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
        }.start();
    }
}