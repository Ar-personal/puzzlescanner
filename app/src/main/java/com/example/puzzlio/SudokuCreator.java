package com.example.puzzlio;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SudokuCreator extends AppCompatActivity{

    private Button[][] gridButtons = new Button[9][9]; //not needed?
    private Button gridButton;
    private ToggleButton toggleButton;
    private boolean locked;
    private CharSequence text;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudokucreator);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.sodukulayout);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //set puzzle start to half the screen minus half the puzzle size - change variables
        int X, Y = (displayMetrics.heightPixels / 2) - ((120 * 9) / 2 ) -200;

        //action bar
        getSupportActionBar().setTitle("Editor Mode");
        getSupportActionBar().setLogo(R.drawable.ic_menu_white_30dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#307BC0")));

        //lockbutton
        toggleButton = findViewById(R.id.lockgrid);


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(toggleButton.isChecked()){
                    locked = true;
                }else{
                    locked = false;
                }
            }
        });


        //puzzle grid
        for(int y = 0; y < gridButtons.length; y++){
                X = 0;
            for(int x = 0; x < gridButtons[y].length; x++){
                final int j = x , k = y;

                gridButtons[x][y] = new Button(this);
                gridButtons[x][y].setX(X);
                gridButtons[x][y].setY(Y);
                gridButtons[x][y].setLayoutParams(new RelativeLayout.LayoutParams(120, 120));

                gridButtons[x][y].setText("");
                gridButtons[x][y].setTextColor(Color.BLACK);
                gridButtons[x][y].setGravity(Gravity.CENTER);
                gridButtons[x][y].setTextSize(25);
                gridButtons[x][y].setPadding(0, 0, 0, 0);
                gridButtons[x][y].setBackgroundResource(R.drawable.gridborder);
                gridButtons[x][y].setInputType(InputType.TYPE_CLASS_NUMBER);







                //set max text length to 1
                int maxLength = 1;
                InputFilter[] fa = new InputFilter[1];
                fa[0] = new InputFilter.LengthFilter(maxLength);
                gridButtons[x][y].setFilters(fa);





                gridButtons[x][y].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!locked) {
                            gridButtons[j][k].setBackgroundColor(Color.GREEN);
                            AlertDialog.Builder builder = new AlertDialog.Builder(SudokuCreator.this);
                            builder.setTitle("Edit");
                            EditText input = new EditText(SudokuCreator.this);
                            input.setInputType(InputType.TYPE_CLASS_NUMBER);
                            builder.setView(input);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    text = input.getText().toString();
                                    gridButtons[j][k].setText(text);
                                    System.out.println(text);
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            builder.show();
                        }else{
                            gridButtons[j][k].setBackgroundColor(Color.RED);
                            Toast.makeText(SudokuCreator.this, "Unlock to edit", Toast.LENGTH_SHORT).show();
                        }
                    }
                });






                System.out.println(gridButtons[x][y].getText());

                relativeLayout.addView(gridButtons[x][y]);

                X += 120;

            }
            Y += 120;

        }

    }



}


