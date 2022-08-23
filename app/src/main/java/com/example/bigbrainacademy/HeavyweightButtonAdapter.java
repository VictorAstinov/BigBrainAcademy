package com.example.bigbrainacademy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;

public class HeavyweightButtonAdapter extends RecyclerView.Adapter<HeavyweightButtonAdapter.ViewHolder> {

    private ArrayList<Integer> data;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;
    private int itemWidth;
    private Context context;

    private boolean buttonsEnabled = false;

    // data is passed into the constructor
    public HeavyweightButtonAdapter(Context context, HashSet<Integer> data, int width) {
        layoutInflater = LayoutInflater.from(context);
        this.data = new ArrayList<Integer> (data);
        itemWidth = width;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recyclerview_weighted_object, parent, false);
        view.getLayoutParams().width = itemWidth; // must change width here!!
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //TODO: add different drawables and make them correspond to id
        Drawable buttonDrawable = context.getDrawable(R.drawable.weighted_object_1);
        holder.weightedObjectButton.setImageDrawable(buttonDrawable);
        holder.weightedObjectButton.setId(data.get(position));
        holder.weightedObjectButton.setEnabled(buttonsEnabled);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return data.size();
    }

    // convenience method for getting data at click position
    public int getItemValue(int id) {
        return data.get(id);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton weightedObjectButton;

        ViewHolder(View itemView) {
            super(itemView);
            weightedObjectButton = itemView.findViewById(R.id.weighted_object_image_button);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public void enableButtons(boolean enabled) {
        buttonsEnabled = enabled;
    }

    // allows clicks events to be caught
    public void setClickListener(HeavyweightButtonAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
