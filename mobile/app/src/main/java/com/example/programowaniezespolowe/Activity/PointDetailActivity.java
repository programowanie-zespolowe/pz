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

import com.example.programowaniezespolowe.Adapter.PointDetailAdapter;
import com.example.programowaniezespolowe.Connection.ConnectWebService;
import com.example.programowaniezespolowe.Data.PointDetail;
import com.example.programowaniezespolowe.Data.PointPath;
import com.example.programowaniezespolowe.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PointDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private GridView gridView;
    private SearchView searchView;
    private int idGroup;
    private int idBuilding;
    private ConnectWebService connectWebService;
    private ArrayList<PointDetail> pointDetailList;
    private PointDetailAdapter pointDetailAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);
        Intent intent = getIntent();
        idGroup = intent.getIntExtra(CategoryActivity.GROUP_ID, 0);
        idBuilding = intent.getIntExtra(ScanCode.BUILDING_ID, 0);
        pointDetailList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new getPointDetail().execute();
        gridView = findViewById(R.id.gridRooms);
        searchView = findViewById(R.id.searchRooms);
        pointDetailAdapter = new PointDetailAdapter(this, pointDetailList);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pointDetailAdapter.getFilter().filter(newText);
                return false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int target = pointDetailList.get(position).getIdPoint();
                PointPath pointPath = PointPath.getInstance();
                pointPath.setTargetPoint(target);
                Intent intent1 = new Intent(PointDetailActivity.this, Navigation_activity.class);
                intent1.putExtra(ScanCode.BUILDING_ID, idBuilding);
                intent1.putExtra(CategoryActivity.GROUP_ID, idGroup);
                startActivity(intent1);
                finish();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent = new Intent(this, CategoryActivity.class);
                intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class getPointDetail extends AsyncTask<Void, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... voids) {
            connectWebService = ConnectWebService.GetInstance();
            return connectWebService.getPointDetail(idGroup);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            String pointDetail = null;
            Gson gson = new Gson();
            for(int i = 0; i < jsonArray.length(); i++){
                try {
                    pointDetail = jsonArray.getString(i);
                    PointDetail point = gson.fromJson(pointDetail, PointDetail.class);
                    pointDetailList.add(point);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            gridView.setAdapter(pointDetailAdapter);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {

        switch(keyCode) {
            case(KeyEvent.KEYCODE_BACK):
                Intent a1_intent = new Intent(this, CategoryActivity.class);
                a1_intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
                startActivity(a1_intent);
                finish();
                return true;
        }
        return false;
    }


}
