package com.example.programowaniezespolowe.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.programowaniezespolowe.Connection.ConnectWebService;
import com.example.programowaniezespolowe.Data.RekordTime;
import com.example.programowaniezespolowe.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class RankingActivity extends AppCompatActivity {
    private TextView twojCzas;
    private TextView ranking;
    private int idGame;
    private Toolbar toolbar;
    private int idBuilding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Intent intent = getIntent();
        String czas = intent.getStringExtra("czas");
        idGame = intent.getIntExtra("idGame", 0);
        idBuilding = intent.getIntExtra(ScanCode.BUILDING_ID, 0);
        twojCzas = findViewById(R.id.twojCzas);
        twojCzas.setText(czas);
        ranking = findViewById(R.id.topTen);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.ranking);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AsyncTask<Void, Void, JSONArray> json = new rekord().execute();
        JSONArray jsonArray = null;
        JSONObject napis = null;
        try {
            jsonArray = json.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int time;
        String text = "";
        Gson gson = new Gson();
        for (int i = 0; i < jsonArray.length(); i++){
            try {
                napis = jsonArray.getJSONObject(i);
                String name = napis.get("name").toString();
                time = (int) napis.get("time");
                int diffSeconds = time % 60;
                int diffMinutes = (time % 3600) / 60;
                int diffHours = time / 3600;
                text += "NickName: " + name + ", czas: " + String.format("%02d:%02d:%02d",diffHours, diffMinutes, diffSeconds) + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ranking.setText(text);

    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        switch(keyCode) {
            case(KeyEvent.KEYCODE_BACK):
                Intent a1_intent = new Intent(this, ChooseActivity.class);
                a1_intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
                startActivity(a1_intent);
                finish();
                return true;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent a1_intent = new Intent(this, ChooseActivity.class);
                a1_intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
                startActivity(a1_intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class rekord extends AsyncTask<Void, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... voids) {
            ConnectWebService connectWebService = ConnectWebService.GetInstance();
            return connectWebService.getRekord(idGame);
        }
    }
}
