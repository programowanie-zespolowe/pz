package com.example.programowaniezespolowe.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.programowaniezespolowe.Adapter.GameAdapter;
import com.example.programowaniezespolowe.Connection.ConnectWebService;
import com.example.programowaniezespolowe.Data.OutdoorGame;
import com.example.programowaniezespolowe.Data.OutdoorGamePath;
import com.example.programowaniezespolowe.Data.OutdoorGameTime;
import com.example.programowaniezespolowe.Data.PointPath;
import com.example.programowaniezespolowe.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class GameListActivity extends AppCompatActivity {

    private GridView gridView;
    private ConnectWebService connectWebService;
    private ArrayList<OutdoorGame> games;

    private int idBuilding;
    private GameAdapter gameAdapter;
    private int idGame;
    private OutdoorGamePath outdoorGamePath;
    private PointPath p;
    private OutdoorGameTime outdoorGameTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        Intent intent = getIntent();
        idBuilding = intent.getIntExtra(ScanCode.BUILDING_ID, 0);
        games = new ArrayList<>();
        gridView = findViewById(R.id.gridGames);
        gameAdapter = new GameAdapter(this, games);
        p = PointPath.getInstance();
        outdoorGameTime = OutdoorGameTime.getInstance();
        new getOutGame().execute();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                idGame = games.get(position).getIdOutdoorGame();
                outdoorGameTime.setIdOutdoorGame(idGame);
                AsyncTask asyncTask = new getTime().execute();
                String test = null;
                try {
                    test = (String) asyncTask.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                Gson gson = gsonBuilder.create();
                Date date = gson.fromJson(test, Date.class);
                outdoorGameTime.setStart(date);

                    int idGamePoint = games.get(position).getIdFirstPoint();
                    intent = new Intent(GameListActivity.this, GameActivity.class);
                    intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
                    intent.putExtra("idGame", idGame);
                    startActivity(intent);

            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case (KeyEvent.KEYCODE_BACK):
                Intent intent = new Intent(GameListActivity.this, ChooseActivity.class);
                intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
                intent.putExtra("idGame", idGame);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }

    private  class getOutGame extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            connectWebService = ConnectWebService.GetInstance();
            return connectWebService.getOutdoorGame(idBuilding);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            String game = null;
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("yyyy-MM-dd");
            Gson gson = gsonBuilder.create();
            for(int i = 0; i < jsonArray.length(); i++){
                try {
                    game = jsonArray.getString(i);
                    games.add(gson.fromJson(game, OutdoorGame.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            gridView.setAdapter((gameAdapter));
        }
    }

    private class getTime extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            connectWebService = ConnectWebService.GetInstance();
            return connectWebService.getOutdoorTime(idGame, outdoorGameTime.getName(), outdoorGameTime.getMacId(), true);
        }

        @Override
        protected void onPostExecute(String s) {
            onCancelled();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
