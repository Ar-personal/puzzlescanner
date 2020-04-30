package com.example.puzzlio;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;

public class Soduku extends AppCompatActivity{

    private EditText[][] textViews = new EditText[9][9]; //not needed?
    private EditText textView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soduku);
        CoordinatorLayout relativeLayout = (CoordinatorLayout) findViewById(R.id.sodukulayout);
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



        //puzzle grid
        for(int y = 0; y < textViews.length; y++){
                X = 0;
            for(int x = 0; x < textViews[y].length; x++){
                textView = new EditText(this);
                textView.setX(X);
                textView.setY(Y);
                textView.setWidth(120);
                textView.setHeight(120);
                textView.setText("");
                textView.setTextColor(Color.BLACK);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(24);
                textView.setPadding(0, 15, 0, 15);
                textView.setBackgroundResource(R.drawable.gridborder);
                textView.setInputType(InputType.TYPE_CLASS_NUMBER);

                //set max text length to 1
                int maxLength = 1;
                InputFilter[] fa = new InputFilter[1];
                fa[0] = new InputFilter.LengthFilter(maxLength);
                textView.setFilters(fa);

                textView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(textView.getText().hashCode() == charSequence.hashCode()) {
                            if (charSequence.length() != 0) {
                                textView.setText(charSequence);
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                relativeLayout.addView(textView);

                X += 120;

            }
            Y += 120;

        }

    }



}


