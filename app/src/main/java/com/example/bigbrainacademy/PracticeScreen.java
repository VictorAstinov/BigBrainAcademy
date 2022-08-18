package com.example.bigbrainacademy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bigbrainacademy.databinding.ActivityPracticeScreenBinding;

// view for the practice menu
public class PracticeScreen extends AppCompatActivity implements ActivityInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_view();
        init_buttons();
    }

    @Override
    public void init_view() {
        ActivityPracticeScreenBinding bind = ActivityPracticeScreenBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        setContentView(view);
        startActivity(new Intent(this, MemoRandomScreen.class));
    }

    @Override
    public void init_buttons() {
    }
}