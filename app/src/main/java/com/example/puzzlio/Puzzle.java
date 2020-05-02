package com.example.puzzlio;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Puzzle extends AppCompatActivity {

    private int image;
    private String title;
    private String description;
    private String dateCreated;
    private String author;
    private int puzzleType;
    private Button[][] gridButtons;
    private RelativeLayout puzzleLayout;
    private Integer[][] arrayValues, arrayLocked, arrayBlack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle);

        puzzleLayout = (RelativeLayout) findViewById(R.id.puzzlelayout);


        Bundle extras = getIntent().getExtras();
        title = extras.getString("title");
        puzzleType = extras.getInt("type");
        arrayValues = (Integer[][]) extras.get("gridValues");
        arrayBlack = (Integer[][]) extras.get("gridLocked");

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setLogo(R.drawable.ic_menu_white_30dp);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#307BC0")));



        switch (puzzleType){
            case 0:
                System.out.println("create crossword");
            case 1:
                createSudokuGrid();
            break;

            case 2:

            break;
        }





    }

    int X, Y;

    private void createSudokuGrid() {
        for(int y = 0; y < arrayValues.length; y++){
            X = 0;
            for(int x = 0; x < arrayValues[y].length; x++){
                final int j = x , k = y;

                gridButtons[x][y] = new Button(this);
                gridButtons[x][y].setX(X);
                gridButtons[x][y].setY(Y);
                gridButtons[x][y].setLayoutParams(new RelativeLayout.LayoutParams(120, 120));

                gridButtons[x][y].setText(arrayValues[x][y].toString());
                gridButtons[x][y].setTextColor(Color.BLACK);
                gridButtons[x][y].setGravity(Gravity.CENTER);
                gridButtons[x][y].setTextSize(28);
                gridButtons[x][y].setPadding(0, 0, 0, 0);
                gridButtons[x][y].setBackgroundResource(R.drawable.gridborder);
                gridButtons[x][y].setInputType(InputType.TYPE_CLASS_NUMBER);

                //set max text length to 1
                int maxLength = 1;
                InputFilter[] fa = new InputFilter[1];
                fa[0] = new InputFilter.LengthFilter(maxLength);
                gridButtons[x][y].setFilters(fa);

                boolean locked;

                if(arrayLocked[x][y] == -1){
                    locked = true;
                }else{
                    locked = false;
                }


                if(!locked) {
                    gridButtons[x][y].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Puzzle.this);
                                builder.setTitle("Edit");
                                EditText input = new EditText(Puzzle.this);
                                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                builder.setView(input);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String text = input.getText().toString();
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
                            }
                    });
                }


                System.out.println(gridButtons[x][y].getText());

                puzzleLayout.addView(gridButtons[x][y]);

                X += 120;

            }
            Y += 120;

        }
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
