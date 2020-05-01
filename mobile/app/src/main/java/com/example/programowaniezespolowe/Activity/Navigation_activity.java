package com.example.programowaniezespolowe.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.programowaniezespolowe.Connection.ConnectWebService;
import com.example.programowaniezespolowe.Data.NextPoint;
import com.example.programowaniezespolowe.Data.OutdoorGamePath;
import com.example.programowaniezespolowe.Data.PointPath;
import com.example.programowaniezespolowe.MyCanavas;
import com.example.programowaniezespolowe.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Navigation_activity extends AppCompatActivity{
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private int idGroup;
    private int idBuilding;
    private int idGame;
    private ConnectWebService connectWebService;
    private NextPoint nextPoint;
    private TextView textView;
    private PointPath pointPath;
    private ImageView iconImage;
    private ImageView stairsOrElevatorImage;
    private ImageView anotherLevelImage;

    private ActionBarDrawerToggle drawerToggle;

    private OutdoorGamePath outdoorGamePath;

    int idPoint;
    int idNextPoint ;
    private String question;
    private String answer;
    private int hintPoint;
    private String hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        Intent intent = getIntent();
        idGroup = intent.getIntExtra(CategoryActivity.GROUP_ID, 0);
        idBuilding = intent.getIntExtra(ScanCode.BUILDING_ID, 0);
        idGame = intent.getIntExtra("idGame", 0);
        idNextPoint = intent.getIntExtra("idNextPoint", -1);
        question = intent.getStringExtra("question");
        answer = intent.getStringExtra("answer");
        hintPoint = intent.getIntExtra("hintPoint", 0);
        hint = intent.getStringExtra("hint");
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
                        pointPath.setPreviousPoint(-1);
                        intent = new Intent(Navigation_activity.this, BuildingsActivity.class);
                        intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
                        startActivity(intent);
                        break;
                    case R.id.pokoje:
                        pointPath.setPreviousPoint(-1);
                        intent = new Intent(Navigation_activity.this, PointDetailActivity.class);
                        intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
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
        iconImage= findViewById(R.id.testImage);
        stairsOrElevatorImage= findViewById(R.id.StairsOrElevator);
        anotherLevelImage = findViewById(R.id.AnotherLvl);
        new getNextPoint().execute();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        switch(keyCode) {
            case(KeyEvent.KEYCODE_BACK):
                Intent a1_intent = new Intent(this, ChooseActivity.class);
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
                textView.setTextSize(20);
                textView.setText("Jestes na miejscu");
                iconImage.setVisibility(View.GONE);
                if(ChooseActivity.getOption() == 0){
                    Intent intent = new Intent(Navigation_activity.this, ScanCode.class);
                    intent.putExtra("idGame", idGame);
                    intent.putExtra("idNextPoint", idNextPoint);
                    intent.putExtra("question", question);
                    intent.putExtra("answer", answer);
                    intent.putExtra("hintPoint", hintPoint);
                    intent.putExtra("hint", hint);
                    startActivity(intent);
                }
        }else{
            new getNextPoint().execute();
        }
    }
    private void dissplayPath(){
        stairsOrElevatorImage.setVisibility(View.GONE);
        anotherLevelImage.setVisibility(View.GONE);
        int id = 0;
        int idText = 0;
        int idStairsOrElevator = 0;
        int idIconOnAnotherLevel = 0;
        String instrukcje = "";
        id = getIdIcon(nextPoint.getIcon());
        idText = getIdText(nextPoint.getIcon());
        iconImage.getLayoutParams().width = 600;
        iconImage.getLayoutParams().height = 600;
        dissplayIcon(iconImage, id);
        instrukcje += getString(idText) + " " + (int)nextPoint.getDistance() + " metrów.";
        if(nextPoint.isStairs() || nextPoint.isEleveator()){
            if(nextPoint.isStairs()){
//                idStairsOrElevator = R.drawable.schody;
                if(nextPoint.getCurrentLevel() < nextPoint.getLevel()){
                    idStairsOrElevator = R.drawable.ikona3;
                    idText = R.string.schody_gora;
                }else{
                    idStairsOrElevator = R.drawable.ikona4;
                    idText = R.string.schody_dol;
                }
            }else{
                idStairsOrElevator = R.drawable.ikona9;
                if(nextPoint.getCurrentLevel() < nextPoint.getLevel()){
                    idText = R.string.winda_gora;
                }else{
                    idText = R.string.winda_dol;
                }
            }
            iconImage.getLayoutParams().width = 350;
            iconImage.getLayoutParams().height = 350;
            dissplayIcon(stairsOrElevatorImage, idStairsOrElevator);
            stairsOrElevatorImage.setVisibility(View.VISIBLE);
            idIconOnAnotherLevel = getIdIcon(nextPoint.getIconOnAnotherLevel());
            dissplayIcon(anotherLevelImage, idIconOnAnotherLevel);
            anotherLevelImage.setVisibility(View.VISIBLE);
            id = getIdText(nextPoint.getIconOnAnotherLevel());
            instrukcje += getString(idText) + " " + nextPoint.getLevel() + ". " + getString(id) + " " + (int)nextPoint.getDistanceOnAnotherLevel() + " metrów.";
        }
        textView.setText(instrukcje);
    }

    private void dissplayIcon(ImageView imageView, int idIcon){
        View v = new MyCanavas(getApplicationContext(), nextPoint, idIcon);
        Bitmap bitmap = Bitmap.createBitmap(600/*width*/, 600/*height*/, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        imageView.setImageBitmap(bitmap);
    }

    private int getIdText(int icon){
        int id = 0;
        if(icon == 0){
            id = R.string.prosto;
        }else if(icon == 1){
            id = R.string.lewo;
        }else if(icon == 2){
            id = R.string.lewo;
        }else if(icon == 3){
            id = R.string.zawracanie;
        }else if(icon == 4){
            id = R.string.prawo;
        }if(icon == 5){
            id = R.string.prawo;
        }
        return id;
    }

    private int getIdIcon(int icon){
        int id = 0;
        if(icon == 0){
            id = R.drawable.przod;
        }else if(icon == 1){
            id = R.drawable.ikona15;
        }else if(icon == 2){
            id = R.drawable.ikona_lewo;
        }else if(icon == 3){
            id =R.drawable.ikona7;
        }else if(icon == 4){
            id = R.drawable.ikona10;
        }if(icon == 5){
            id = R.drawable.ikona14;
        }
        return id;
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
            dissplayPath();
        }
    }



}
