package com.example.puzzlio;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Grid extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
    }

    private TextView textView;

    public Grid(){
        textView = findViewById(R.id.gridText);
        textView.setText(null);
    }

    public Grid(Character c){
        textView = findViewById(R.id.gridText);
        textView.setText(null);
    }
}
