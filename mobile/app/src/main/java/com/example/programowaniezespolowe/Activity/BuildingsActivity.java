package com.example.programowaniezespolowe.Activity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.programowaniezespolowe.R;

public class BuildingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private GridView gridView;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildings);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridView = findViewById(R.id.gridBuildings);
        searchView = findViewById(R.id.searchBuildings);



    }


}
