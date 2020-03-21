package com.example.programowaniezespolowe.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.programowaniezespolowe.Data.PointPath;
import com.example.programowaniezespolowe.MyCanavas;
import com.example.programowaniezespolowe.R;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.Result;

import org.json.JSONArray;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Navigation_activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private int idGroup;
    private int idBuilding;
    private ZXingScannerView scannerView;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        Intent intent = getIntent();
        idGroup = intent.getIntExtra(CategoryActivity.GROUP_ID, 0);
        idBuilding = intent.getIntExtra(MainActivity.BUILDING_ID, 0);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDrawer = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        nvDrawer = findViewById(R.id.nvView);
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent intent;
                switch(menuItem.getItemId()){
                    case R.id.budynki:
                        intent = new Intent(Navigation_activity.this, BuildingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.pokoje:
                        intent = new Intent(Navigation_activity.this, PointDetailActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nowy_kod:
                        intent = new Intent(Navigation_activity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        View v = new MyCanavas(getApplicationContext());
        Bitmap bitmap = Bitmap.createBitmap(500/*width*/, 500/*height*/, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        ImageView imageView = findViewById(R.id.testImage);
        imageView.setImageBitmap(bitmap);
//        imageView.setVisibility(View.INVISIBLE);
//



//        ViewGroup contentFrame = findViewById(R.id.frame);
//        scannerView = new ZXingScannerView(this);
//        contentFrame.addView(scannerView);
////        getSupportFragmentManager().beginTransaction().add(R.id.test_fragment,qrScanner);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
//        }
//        scannerView.setVisibility(View.GONE);
//        imageView.setVisibility(View.VISIBLE);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                //Intent intent = new Intent(this, BuildingsActivity.class);
                //intent.putExtra(BuildingsActivity.BUILDING_ID, buildingId);
                //startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {

        switch(keyCode) {
            case(KeyEvent.KEYCODE_BACK):
                Intent a1_intent = new Intent(this, BuildingsActivity.class);
                //a1_intent.putExtra(MainActivity.BUILDING_ID, buildingId);
                startActivity(a1_intent);
                finish();
                return true;
        }
        return false;
    }
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public void handleResult(Result rawResult) {
        if(rawResult != null)
            scannerView.stopCamera();
        String result = rawResult.toString();
        String[] list = result.split(":");
        idBuilding = Integer.parseInt(list[1]);
        int pointNumber = Integer.parseInt(list[2]);
        PointPath pointPath = PointPath.getInstance();
        pointPath.setCurrentPoint(pointNumber);

    }

    private class sendPoint extends AsyncTask<Void, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... voids) {
            return null;
        }
    }
}
