package com.example.bigbrainacademy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bigbrainacademy.databinding.ActivityHeavyweightScreenBinding;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class HeavyweightScreen extends AbstractActivity implements HeavyweightButtonAdapter.ItemClickListener {

    //TODO: randomize order of how objects appear (only appearance)

    private HeavyweightGame heavyweightState;
    private View view;
    private HeavyweightScaleAdapter scaleAdapter;
    private RecyclerView scaleRecyclerView;
    private HeavyweightButtonAdapter buttonAdapter;
    private RecyclerView buttonRecyclerView;

    private ArrayList<Drawable> scaleDrawables;
    private HashSet<Integer> uniqueObjectIds;

    //private CountDownTimer timer = null;

    private final String WEIGHTED_OBJECT_ID = "drawable/weighted_object_";
    private final String WEIGHTED_OBJECT_FILETYPE = ".png";

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
        init_view();

        HeavyweightScreen.CustomGridLayoutManager layoutManager = new HeavyweightScreen.CustomGridLayoutManager(this);
        layoutManager.setScrollEnabled(false);

        scaleRecyclerView = findViewById(R.id.heavyweight_recycleView);
        scaleRecyclerView.setLayoutManager(layoutManager);
        scaleRecyclerView.setNestedScrollingEnabled(false);

        HeavyweightScreen.CustomGridLayoutManager layoutManagerButtons = new HeavyweightScreen.CustomGridLayoutManager(this);
        layoutManagerButtons.setScrollEnabled(false);

        buttonRecyclerView = findViewById(R.id.heavyweight_recycleViewButtons);
        buttonRecyclerView.setLayoutManager(layoutManagerButtons);
        buttonRecyclerView.setNestedScrollingEnabled(false);

        run();
        init_buttons();
    }

    @Override
    protected void init_view() {
        ActivityHeavyweightScreenBinding bind = ActivityHeavyweightScreenBinding.inflate(getLayoutInflater());
        view = bind.getRoot();
        setContentView(view);
    }

    @Override
    protected void init_buttons() {

    }

    @Override
    protected void toggle_buttons(boolean enable) {
        buttonAdapter.enableButtons(enable);
        buttonAdapter.notifyDataSetChanged();
    }

    private void run() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        scaleDrawables = new ArrayList<>();
        uniqueObjectIds = new HashSet<>();
        heavyweightState = new HeavyweightGame();
        setScales();
        scaleAdapter = new HeavyweightScaleAdapter(this, scaleDrawables, displayMetrics.widthPixels);
        scaleRecyclerView.setAdapter(scaleAdapter);
        buttonAdapter = new HeavyweightButtonAdapter(this, uniqueObjectIds, displayMetrics.widthPixels);
        buttonAdapter.setClickListener(this);
        toggle_buttons(true);
        buttonRecyclerView.setAdapter(buttonAdapter);
    }

    private void setScales() {
        for (int i = 0; i < heavyweightState.getNumScales(); ++i) {
            HeavyweightGame.ScaleStatus scaleStatus = heavyweightState.getScaleStatus(i);
            ArrayList<Integer> leftObjectIds = heavyweightState.getLeftObjectIds(i);
            ArrayList<Integer> rightObjectIds = heavyweightState.getRightObjectIds(i);

            determineUniqueObjectIds(leftObjectIds, rightObjectIds);

            Drawable scaleImage = createScaleImage(scaleStatus, leftObjectIds, rightObjectIds);
            scaleDrawables.add(scaleImage);
        }
    }

    private Drawable createScaleImage(HeavyweightGame.ScaleStatus scaleStatus,
                                            ArrayList<Integer> leftIds, ArrayList<Integer> rightIds) {
        Bitmap emptyScaleBitmap;
        switch(scaleStatus) {
            case BALANCED:
                emptyScaleBitmap = drawableToBitmap(getDrawable(R.drawable.scale_balanced));
                break;
            //TODO: add other orientations
            case LEANING_LEFT:
                emptyScaleBitmap = drawableToBitmap(getDrawable(R.drawable.scale_balanced));
                break;
            case LEANING_RIGHT:
                emptyScaleBitmap = drawableToBitmap(getDrawable(R.drawable.scale_balanced));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + scaleStatus);
        }
        Bitmap leftPanObjectsBitmap = stackedObjectsBitmap(leftIds);
        Bitmap rightPanObjectsBitmap = stackedObjectsBitmap(leftIds);
        Drawable scaleImage = combineScaleObjectsBitmaps(emptyScaleBitmap, leftPanObjectsBitmap, rightPanObjectsBitmap, scaleStatus);
        return scaleImage;
    }

    private Bitmap stackedObjectsBitmap(ArrayList<Integer> idsList) {
        switch (idsList.size()) {
            case 1:
                //TODO: add different images
                return drawableToBitmap(getDrawable(R.drawable.weighted_object_1));
            default:
                throw new IllegalStateException();
        }
    }

    private Drawable combineScaleObjectsBitmaps(Bitmap scale, Bitmap leftPan, Bitmap rightPan, HeavyweightGame.ScaleStatus scaleStatus) {
        //TODO: make this work on different devices i.e. resize
        int scaleWidth = scale.getWidth();
        int scaleHeight = scale.getHeight();
        Bitmap combinedBitmap = Bitmap.createBitmap(scaleWidth, scaleHeight, scale.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);
        canvas.drawBitmap(scale, new Matrix(), null);

        canvas.drawBitmap(leftPan, 110, 90, null);
        canvas.drawBitmap(rightPan, 440, 90, null);

        Drawable combinedScaleImage = new BitmapDrawable(getResources(), combinedBitmap);
        return combinedScaleImage;
    }

    private void determineUniqueObjectIds(ArrayList<Integer> leftIds, ArrayList<Integer> rightIds) {
        uniqueObjectIds.addAll(leftIds);
        uniqueObjectIds.addAll(rightIds);
    }

    @Override
    public void onItemClick (View view, int position) {
        int buttonId = buttonAdapter.getItemValue(position);
        System.out.println(buttonId);
    }

    @Override
    protected Intent moveToResultsScreen() {
        return null;
    }
}
