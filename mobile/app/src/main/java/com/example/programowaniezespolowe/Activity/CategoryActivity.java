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

    public static final String GROUP_ID = "groupId";
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
        buildingId = intent.getIntExtra(ScanCode.BUILDING_ID, 0);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridView = findViewById(R.id.gridCategory);
        searchView = findViewById(R.id.searchCategory);
        adapterGroup = new AdapterGroup(this, groupList);
        new getGroups().execute();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterGroup.getFilter().filter(newText);
                return false;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idGroup = groupList.get(position).getIdGroup();
                Intent intent1;
                intent1 = new Intent(CategoryActivity.this, PointDetailActivity.class);
                intent1.putExtra(GROUP_ID, idGroup);
                intent1.putExtra(ScanCode.BUILDING_ID, buildingId);
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
           gridView.setAdapter(adapterGroup);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent = new Intent(this, ChooseActivity.class);
                intent.putExtra(ScanCode.BUILDING_ID, buildingId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                CategoryActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {

        switch(keyCode) {
            case(KeyEvent.KEYCODE_BACK):
                Intent intent = new Intent(this, ChooseActivity.class);
                intent.putExtra(ScanCode.BUILDING_ID, buildingId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                CategoryActivity.this.finish();
                return true;
        }
        return false;
    }
}
