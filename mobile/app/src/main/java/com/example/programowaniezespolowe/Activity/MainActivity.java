package com.example.programowaniezespolowe.Activity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
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
import com.example.programowaniezespolowe.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;


import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ZXingScannerView scannerView;

    private ActionBarDrawerToggle drawerToggle;
    private Button searchBuildings;

    public static final String BUILDING_ID = "buildingId";
    private ConnectWebService connect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        Device device = new Device(Build.MANUFACTURER + Build.MODEL, info.getMacAddress());
        connect = ConnectWebService.GetInstance();
        connect.setDevice(device);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title);

        mDrawer = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        nvDrawer = findViewById(R.id.nvView);
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent = null;
                int id = menuItem.getItemId();
                System.out.println(id);
                switch (menuItem.getItemId()) {
                    case R.id.budynki:
                        intent = new Intent(MainActivity.this, BuildingsActivity.class);
                        break;
                    case R.id.pokoje:
                        intent = new Intent(MainActivity.this, RoomsActivity.class);
                        break;
                }
                startActivity(intent);
                return true;
            }
        });

        searchBuildings = findViewById(R.id.searchBtn);
        searchBuildings.setOnClickListener(searchClickListener);

        ViewGroup contentFrame = findViewById(R.id.content_frame);
        scannerView = new ZXingScannerView(this);
        contentFrame.addView(scannerView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
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


    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private View.OnClickListener searchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, BuildingsActivity.class);
            startActivity(intent);
        }
    };


    @Override
    public void handleResult(Result rawResult) {
        if(rawResult != null)
            scannerView.stopCamera();
        String result = rawResult.toString();
        String[] list = result.split(":");
        int idBuilding = Integer.parseInt(list[1]);
        int pointNumber = Integer.parseInt(list[2]);

        startCategoryActivity(idBuilding);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scannerView.resumeCameraPreview(MainActivity.this);
            }
        }, 2000);

    }

    private void startCategoryActivity(int idBuilding){
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        intent.putExtra("buildingId", idBuilding);
        startActivity(intent);
    }
}
