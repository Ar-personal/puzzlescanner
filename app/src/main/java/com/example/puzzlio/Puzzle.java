package com.example.puzzlio;

import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Puzzle extends AppCompatActivity {

    public int image;
    public String name;
    public String description;
    public String dateCreated;
    public String author;
    public int puzzleType;
    public int[][] dims;
    public Button[][] buttons;

    public Puzzle(int puzzleType, int[][] dims, String name){
        this.puzzleType = puzzleType;
        this.dims = dims;
        this.name = name;

        if(puzzleType == 0){
            createCrossword(dims);
        }else if(puzzleType == 1){
            createSoduku();
        }else{
            System.out.println("invalid puzzle type");
        }

    }

    public void createCrossword(int[][] d){
        for(int y = 0; y < d.length; y++){
            for(int x = 0; x < d[y].length; x++){
                buttons[x][y] = new Button(this);
            }
        }
    }

    public void createSoduku(){

    }

    public int getPuzzleType() {
        return puzzleType;
    }

    public void setPuzzleType(int puzzleType) {
        this.puzzleType = puzzleType;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
