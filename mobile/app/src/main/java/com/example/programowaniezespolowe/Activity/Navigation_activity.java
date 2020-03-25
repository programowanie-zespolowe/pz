package com.example.programowaniezespolowe.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.programowaniezespolowe.Connection.ConnectWebService;
import com.example.programowaniezespolowe.Data.NextPoint;
import com.example.programowaniezespolowe.Data.PointPath;
import com.example.programowaniezespolowe.MyCanavas;
import com.example.programowaniezespolowe.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class Navigation_activity extends AppCompatActivity{
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private int idGroup;
    private int idBuilding;
    private ConnectWebService connectWebService;
    private NextPoint nextPoint;
    private TextView textView;
    private Button iAmHere;
    private PointPath pointPath;
    private ImageView image;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        Intent intent = getIntent();
        idGroup = intent.getIntExtra(CategoryActivity.GROUP_ID, 0);
        idBuilding = intent.getIntExtra(MainActivity.BUILDING_ID, 0);
        pointPath = PointPath.getInstance();
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
                        intent.putExtra(MainActivity.BUILDING_ID, idBuilding);
                        startActivity(intent);
                        break;
                    case R.id.pokoje:
                        intent = new Intent(Navigation_activity.this, PointDetailActivity.class);
                        intent.putExtra(MainActivity.BUILDING_ID, idBuilding);
                        intent.putExtra(CategoryActivity.GROUP_ID, idGroup);
                        startActivity(intent);
                        break;
                    case R.id.nowy_kod:
                        intent = new Intent(Navigation_activity.this, MainActivity.class);
                        pointPath.setPreviousPoint(-1);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        textView = findViewById(R.id.instruction);
        //image = findViewById(R.id.testImage);
        new getNextPoint().execute();
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

    public void acceptHere(View view) {
        pointPath.setPreviousPoint(pointPath.getCurrentPoint());
        pointPath.setCurrentPoint(pointPath.getNextPoint());
        if(pointPath.getTargetPoint() == pointPath.getCurrentPoint()){
            textView.setText("Jestes na miejscu");
        }else{
            new getNextPoint().execute();
        }
    }

    private void dissplayNestPoint(){
        View v = new MyCanavas(getApplicationContext(), nextPoint);
        Bitmap bitmap = Bitmap.createBitmap(700/*width*/, 700/*height*/, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        ImageView imageView = findViewById(R.id.testImage);
        imageView.setImageBitmap(bitmap);
        textView.setText("");
        String napis;
        if(pointPath.getNextPoint() == pointPath.getTargetPoint()){
            //textView.setText("Juz prawie jesteś!");
            napis ="Juz prawie jesteś!\n" +
             "idPoint: " + nextPoint.getIdPoint() + "\n" +
                    "Icon: " + nextPoint.getIcon() + "\n" +
                    "Angle: " + nextPoint.getAngle() + "\n" +
                    "Distance: " + nextPoint.getDistance() + "\n" +
                    "Elevator: " + nextPoint.isEleveator() + "\n" +
                    "Stairs: " + nextPoint.isStairs() + "\n" +
                    "Level: " + nextPoint.getLevel() + "\n" +
                    "IconOnAnotherLevel: " + nextPoint.getIconOnAnotherLevel() + "\n" +
                    "AngleOnAnotherLevel: " + nextPoint.getAngleOnAnotherLevel() + "\n" +
                    "DistanceOnAnotherLevel: " + nextPoint.getDistanceOnAnotherLevel() + "\n";
        }else {

            napis = "idPoint: " + nextPoint.getIdPoint() + "\n" +
                    "Icon: " + nextPoint.getIcon() + "\n" +
                    "Angle: " + nextPoint.getAngle() + "\n" +
                    "Distance: " + nextPoint.getDistance() + "\n" +
                    "Elevator: " + nextPoint.isEleveator() + "\n" +
                    "Stairs: " + nextPoint.isStairs() + "\n" +
                    "Level: " + nextPoint.getLevel() + "\n" +
                    "IconOnAnotherLevel: " + nextPoint.getIconOnAnotherLevel() + "\n" +
                    "AngleOnAnotherLevel: " + nextPoint.getAngleOnAnotherLevel() + "\n" +
                    "DistanceOnAnotherLevel: " + nextPoint.getDistanceOnAnotherLevel() + "\n";
        }
        textView.setText(napis);
    }

    private class getNextPoint extends AsyncTask<Void, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {
            connectWebService = ConnectWebService.GetInstance();
            return connectWebService.getNextPoint(idBuilding);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Gson gson = new Gson();
            String next = null;
            next = jsonObject.toString();
            nextPoint = gson.fromJson(next, NextPoint.class);
            pointPath.setNextPoint(nextPoint.getIdPoint());
            dissplayNestPoint();
        }
    }

}
