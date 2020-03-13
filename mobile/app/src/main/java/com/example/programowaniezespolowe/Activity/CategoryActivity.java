package com.example.programowaniezespolowe.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.programowaniezespolowe.Adapter.AdapterGroup;
import com.example.programowaniezespolowe.Connection.ConnectWebService;
import com.example.programowaniezespolowe.Data.Group;
import com.example.programowaniezespolowe.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    public static final String BUILDING_ID = "buildingId";
    private Toolbar toolbar;
    private GridView gridView;
    private SearchView searchView;
    private ArrayList<Group> groupList;
    private ConnectWebService connectWebService;

    private int buildingId;
    private  AdapterGroup adapterGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        groupList = new ArrayList<>();
        Intent intent = getIntent();
        buildingId = intent.getIntExtra(MainActivity.BUILDING_ID, 0);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new getGroups().execute();
        gridView = findViewById(R.id.gridCategory);
        adapterGroup = new AdapterGroup(this, groupList);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = groupList.get(position).getNameGroup();
                Intent intent1 = null;
                switch (name){
                    case "Pokoje":
                        intent1 = new Intent(CategoryActivity.this, RoomsActivity.class);
                        intent1.putExtra(BUILDING_ID, buildingId);
                        break;
                }
                startActivity(intent1);
            }
        });

    }

    private class getGroups extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            connectWebService = ConnectWebService.GetInstance();
            return connectWebService.getGroups(buildingId);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
                String category = null;
                Gson gson = new Gson();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        category = jsonArray.getString(i);
                        groupList.add(gson.fromJson(category, Group.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for(Group e: groupList){
                    System.out.println(e);
                }
           gridView.setAdapter(adapterGroup);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent = new Intent(this, BuildingsActivity.class);
                intent.putExtra(BuildingsActivity.BUILDING_ID, buildingId);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {

        switch(keyCode) {
            case(KeyEvent.KEYCODE_BACK):
                Intent a1_intent = new Intent(this, BuildingsActivity.class);
                startActivity(a1_intent);
                finish();
                return true;
        }
        return false;
    }
}
