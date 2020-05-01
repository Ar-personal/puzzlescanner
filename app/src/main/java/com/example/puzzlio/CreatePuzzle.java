package com.example.puzzlio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreatePuzzle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView x, y;
    private int puzzleType, dimX, dimY;
    private Button createButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createpuzzle);

        Spinner spinner = findViewById(R.id.puzzleTypeSpinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.items, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);

        x = findViewById(R.id.dimensionX);
        y = findViewById(R.id.dimensionY);

        x.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0) {
                    dimX = Integer.parseInt(String.valueOf(charSequence));
                    System.out.println("dim X: " + dimX);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        y.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0) {
                    dimY = Integer.parseInt(String.valueOf(charSequence));
                    System.out.println("dim Y: " + dimY);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        createButton = findViewById(R.id.buttonCreate);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(puzzleType == 1) {
                    Intent intent = new Intent(CreatePuzzle.this, SudokuCreator.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int index =  adapterView.getSelectedItemPosition();

        if(index == 0){
            puzzleType = index;
            System.out.println("crossword?");
        }

        if(index == 1){
            puzzleType = index;
            x.setText(String.valueOf(9));
            y.setText(String.valueOf(9));
            System.out.println("soduku?");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
