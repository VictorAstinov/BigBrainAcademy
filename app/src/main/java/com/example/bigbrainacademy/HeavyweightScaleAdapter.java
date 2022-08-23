package com.example.bigbrainacademy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import androidx.annotation.NonNull;

import java.util.ArrayList;

public class HeavyweightScaleAdapter extends RecyclerView.Adapter<HeavyweightScaleAdapter.ViewHolder> {

    private ArrayList<Drawable> data;
    private LayoutInflater layoutInflater;
    private int itemWidth;

    // data is passed into the constructor
    HeavyweightScaleAdapter(Context context, ArrayList<Drawable> data, int width) {
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
        itemWidth = width;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recyclerview_scale, parent, false);
        view.getLayoutParams().width = itemWidth; // must change width here!!
        return new ViewHolder(view);
    }

    // binds the data to each ImageView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.scaleView.setImageDrawable(data.get(position));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return data.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView scaleView;

        ViewHolder(View itemView) {
            super(itemView);
            scaleView = itemView.findViewById(R.id.scale_image_view);
        }
    }
}
