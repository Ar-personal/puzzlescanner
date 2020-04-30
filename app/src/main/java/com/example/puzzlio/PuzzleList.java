package com.example.puzzlio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puzzlio.R;

import java.util.ArrayList;

public class PuzzleList extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.puzzles);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> puzzleList = new ArrayList<>();
        puzzleList.add("Horse");
        puzzleList.add("Cow");
        puzzleList.add("Camel");
        puzzleList.add("Sheep");
        puzzleList.add("Goat");
        puzzleList.add("Horse");
        puzzleList.add("Cow");
        puzzleList.add("Camel");
        puzzleList.add("Sheep");
        puzzleList.add("Goat");
        puzzleList.add("Horse");
        puzzleList.add("Sheep");
        puzzleList.add("Sheep");
        puzzleList.add("Sheep");
        puzzleList.add("Sheep");
        puzzleList.add("Sheep");
        puzzleList.add("Sheep");

        ArrayList<ImageView> imgs = new ArrayList<>();



        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(puzzleList);
        recyclerView.setAdapter(recyclerViewAdapter);


        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);



    }
}
