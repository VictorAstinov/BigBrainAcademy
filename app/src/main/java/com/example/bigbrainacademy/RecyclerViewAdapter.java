package com.example.bigbrainacademy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Integer> data;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;
    private ItemTouchListener itemTouchListener;
    private int itemWidth;

    // data is passed into the constructor
    RecyclerViewAdapter(Context context, ArrayList<Integer> data, int width) {
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
        itemWidth = width;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        view.getLayoutParams().width = itemWidth; // must change width here!!
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTextView.setText(String.valueOf(data.get(position)));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return data.size();
    }


    // stores and recycles views as they are scrolled off screen, for click events only
    // OnTouchListener may not be needed
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.info_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (itemTouchListener != null) {
                return itemTouchListener.onItemTouch(view, getAdapterPosition(), event);
            }
            else {
                return true;
            }
        }
    }


    // convenience method for getting data at click position
    Integer getItemValue(int id) {
        return data.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    void setTouchListener(ItemTouchListener itemTouchListener) {
        this.itemTouchListener = itemTouchListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface ItemTouchListener {
        boolean onItemTouch(View view, int position, MotionEvent motionEvent);
    }
}
