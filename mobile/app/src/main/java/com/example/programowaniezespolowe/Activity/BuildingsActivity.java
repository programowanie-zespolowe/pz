package com.example.programowaniezespolowe.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.programowaniezespolowe.Adapter.BuildingAdapter;
import com.example.programowaniezespolowe.Connection.ConnectWebService;
import com.example.programowaniezespolowe.Data.Building;
import com.example.programowaniezespolowe.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BuildingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private GridView gridView;
    private SearchView searchView;
    private ArrayList<Building> buildingList;
    private ConnectWebService connect;

    private int buildingId;
    private BuildingAdapter buildingAdapter;
    public static final String BUILDING_ID = "buildingId";
    public static final String BUILDING_LIST = "buildingList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildings);
        connect = ConnectWebService.GetInstance();
        final Intent intent = getIntent();
        buildingId = intent.getIntExtra(BUILDING_ID, 0);
        loadData();
        new getBuilding().execute();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchView = findViewById(R.id.searchBuildings);
        gridView = findViewById(R.id.gridBuildings);
        buildingAdapter = new BuildingAdapter(this, buildingList);
//        gridView.setAdapter(buildingAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int buildingChoose = buildingList.get(position).getIdBuilding();
                Intent intent1 = new Intent(BuildingsActivity.this, CategoryActivity.class);
                intent1.putExtra(CategoryActivity.BUILDING_ID, buildingChoose);
                startActivity(intent1);
            }
        });

    }

    private class getBuilding extends AsyncTask<Void, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... voids) {
            connect = ConnectWebService.GetInstance();
            return connect.getSelectedBuilding(buildingId);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            Gson gson = new Gson();
            String b = null;
            try {
                b = jsonArray.getString(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            saveData(gson.fromJson(b, Building.class));
            gridView.setAdapter(buildingAdapter);
        }
    }

    private void loadData() {
            SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(BUILDING_LIST, null);
            Type type = new TypeToken<ArrayList<Building>>() {
            }.getType();
            buildingList = gson.fromJson(json, type);
            if(buildingList == null){
                buildingList = new ArrayList<>();
            }
    }
    private void saveData(Building building) {
        if (!isOnList(building.getIdBuilding())) {
            buildingList.add(building);
            SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(buildingList);
            editor.putString(BUILDING_LIST, json);
            editor.apply();
        }
    }

    private boolean isOnList(int buildingId) {
        if (buildingList == null) {
            buildingList = new ArrayList<>();
            return false;
        }
        for (int i = 0; i < buildingList.size(); i++) {
            if (buildingList.get(i).getIdBuilding() == buildingId) {
                return true;
            }
        }
        return false;
    }
}
