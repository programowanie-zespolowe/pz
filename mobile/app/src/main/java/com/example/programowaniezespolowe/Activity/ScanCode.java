package com.example.programowaniezespolowe.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.programowaniezespolowe.Connection.ConnectWebService;
import com.example.programowaniezespolowe.Data.Building;
import com.example.programowaniezespolowe.Data.Device;
import com.example.programowaniezespolowe.Data.OutdoorGameTime;
import com.example.programowaniezespolowe.Data.PointPath;
import com.example.programowaniezespolowe.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCode extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private Toolbar toolbar;

    private ZXingScannerView scannerView;
    private ArrayList<Building> buildingList;


    public static final String BUILDING_ID = "buildingId";
    private ConnectWebService connect;
    private PointPath pointPath;
    private int idBuilding;
    private int idGame;
    private int idNextPoint;
    private String question;
    private String answer;
    private int hintPoint;
    private String hint;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        Intent intent = getIntent();
        idGame = intent.getIntExtra("idGame", 0);
        idNextPoint = intent.getIntExtra("idNextPoint", -1);
        question = intent.getStringExtra("question");
        answer = intent.getStringExtra("answer");
        hintPoint = intent.getIntExtra("hintPoint", 0);
        hint = intent.getStringExtra("hint");
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        Device device = new Device(Build.MANUFACTURER + Build.MODEL, info.getMacAddress());
        OutdoorGameTime o = OutdoorGameTime.getInstance();
        o.setMacId(device.getMacId());
        connect = ConnectWebService.GetInstance();
        connect.setDevice(device);
//        loadData();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
        }

//        mDrawer = findViewById(R.id.drawer_layout);
//        drawerToggle = setupDrawerToggle();
//        drawerToggle.setDrawerIndicatorEnabled(true);
//        mDrawer.addDrawerListener(drawerToggle);
//        drawerToggle.syncState();
//        nvDrawer = findViewById(R.id.nvView);
//        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                int id = menuItem.getItemId();
//                Intent intent;
//                System.out.println(id);
//                switch (menuItem.getItemId()) {
//                    case R.id.budynki:
//                        intent = new Intent(ScanCode.this, BuildingsActivity.class);
//                        startActivity(intent);
//                        break;
//                    case R.id.pokoje:
//                        intent = new Intent(ScanCode.this, PointDetailActivity.class);
//                        startActivity(intent);
//                        break;
//                }
//                return true;
//            }
//        });

//        searchBuildings = findViewById(R.id.searchBtn);
//        searchBuildings.setOnClickListener(searchClickListener);

        ViewGroup contentFrame = findViewById(R.id.content_frame);
        scannerView = new ZXingScannerView(this);
        contentFrame.addView(scannerView);

    }

    @Override
    protected void onResume() {
        scannerView.setResultHandler(this);
        scannerView.startCamera();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCameraPreview();
        scannerView.stopCamera();
    }


//    private ActionBarDrawerToggle setupDrawerToggle() {
//        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
//    }
//
//    private View.OnClickListener searchClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(ScanCode.this, BuildingsActivity.class);
//            startActivity(intent);
//        }
//    };


    @Override
    public void handleResult(Result rawResult) {
        if(rawResult != null)
            scannerView.stopCamera();
        String result = rawResult.toString();
        String[] list = result.split(":");
        idBuilding = Integer.parseInt(list[1]);
        int pointNumber = Integer.parseInt(list[2]);
        new getBuilding().execute();
        pointPath = pointPath.getInstance();
        pointPath.setCurrentPoint(pointNumber);
        startActivity(idBuilding);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scannerView.resumeCameraPreview(ScanCode.this);
            }
        }, 2000);

    }
//    private void saveData(Building building) {
//        if(!isOnList(building.getIdBuilding())){
//            buildingList.add(building);
//            SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            Gson gson = new Gson();
//            String json = gson.toJson(buildingList);
//            editor.putString(BuildingsActivity.BUILDING_LIST, json);
//            editor.apply();
//        }
//    }
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

//    private void loadData() {
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString(BuildingsActivity.BUILDING_LIST, null);
//        Type type = new TypeToken<ArrayList<Building>>() {
//        }.getType();
//        buildingList = gson.fromJson(json, type);
//        if(buildingList == null){
//            buildingList = new ArrayList<>();
//        }
//    }
    private class getBuilding extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            connect = ConnectWebService.GetInstance();
            return connect.getSelectedBuilding(idBuilding);
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
//            if(b != null) {
//                saveData(gson.fromJson(b, Building.class));
//            }
        }
    }

    private void startActivity(int idBuilding){
        Intent intent = new Intent();
        if(ChooseActivity.getOption() == 1) {
            intent = new Intent(ScanCode.this, CategoryActivity.class);
//            ChooseActivity.getOption() == 0 &&
        }else if(idGame == 0) {
            intent = new Intent(ScanCode.this, ChooseActivity.class);
        } else if(idGame != 0){
            intent = new Intent(ScanCode.this, GameActivity.class);
            intent.putExtra("idNextPoint", idNextPoint);
            intent.putExtra("question", question);
            intent.putExtra("answer", answer);
            intent.putExtra("hintPoint", hintPoint);
            intent.putExtra("hint", hint);
        }
//        else{
//            intent = new Intent(ScanCode.this, ChooseActivity.class);
//        }
        intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
        intent.putExtra("idGame", idGame);
        startActivity(intent);
    }
}
