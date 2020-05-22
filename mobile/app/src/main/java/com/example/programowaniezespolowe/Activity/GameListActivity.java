package com.example.programowaniezespolowe.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.programowaniezespolowe.Adapter.GameAdapter;
import com.example.programowaniezespolowe.Connection.ConnectWebService;
import com.example.programowaniezespolowe.Data.OutdoorGame;
import com.example.programowaniezespolowe.Data.OutdoorGameTime;
import com.example.programowaniezespolowe.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class GameListActivity extends AppCompatActivity {

    final Context context = this;
    private GridView gridView;
    private ConnectWebService connectWebService;
    private ArrayList<OutdoorGame> games;

    private int idBuilding;
    private GameAdapter gameAdapter;
    private int idGame;
    private int loadIdGame;
    private OutdoorGame outdoorGame;
    private OutdoorGameTime outdoorGameTime;
    private String nickName;
    private Toolbar toolbar;
    private int positionClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        Intent intent = getIntent();
        idBuilding = intent.getIntExtra(ScanCode.BUILDING_ID, 0);
        games = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.wybierzGre);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        gridView = findViewById(R.id.gridGames);
        gameAdapter = new GameAdapter(this, games);
        outdoorGameTime = OutdoorGameTime.getInstance();
        new getOutGame().execute();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idGame = games.get(position).getIdOutdoorGame();
                if(isGameFinished()){
                    positionClicked = position;
                    dialog();
                }
            }
        });
    }

    private void startGameActivity(int position) {
        Intent intent = null;
        GameActivity.gameName = games.get(position).getNameGame();
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
        String liczba = test.toString();
        if(liczba.equals("2")) {
            Toast.makeText(getApplicationContext(), "Nick zajęty", Toast.LENGTH_SHORT).show();
            dialog();

        } else{
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
            Gson gson = gsonBuilder.create();
            Date date = gson.fromJson(test, Date.class);
            outdoorGameTime.setStart(date);
            intent = new Intent(GameListActivity.this, GameActivity.class);
            intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
            intent.putExtra("idGame", idGame);
            startActivity(intent);
        }
    }

    private boolean isGameFinished(){
        AsyncTask asyncTask = new getRekordTime().execute();
        String odp= null;
        try {
            odp = (String) asyncTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(odp!= null){
            double czasDouble = Double.parseDouble(odp);
            int czas = (int)czasDouble;
            startRankingActivity(czas);
            return false;
        }
        return true;
    }

    private void startRankingActivity(int czas){
        long diffSeconds = czas % 60;
        long diffMinutes = (czas % 3600) / 60;
        long diffHours = czas / 3600;
        String napis = String.format("Twój czas:  %02d:%02d:%02d",diffHours, diffMinutes, diffSeconds);
        Intent intent = new Intent(GameListActivity.this, RankingActivity.class);
        intent.putExtra("czas", napis);
        intent.putExtra("idGame", idGame);
        intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
        startActivity(intent);
    }

    private void dialog(){
        LayoutInflater layoutinflater = LayoutInflater.from(context);
        View promptUserView = layoutinflater.inflate(R.layout.dialog_user, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptUserView);
        final EditText userAnswer = (EditText) promptUserView.findViewById(R.id.user_name);
        alertDialogBuilder.setTitle("Podaj nick");
        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                nickName = String.valueOf(userAnswer.getText());
                outdoorGameTime.setName(nickName);
                startGameActivity(positionClicked);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case (KeyEvent.KEYCODE_BACK):
                Intent intent = new Intent(GameListActivity.this, ChooseActivity.class);
                intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
                intent.putExtra("idGame", idGame);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                GameListActivity.this.finish();
                return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent = new Intent(GameListActivity.this, ChooseActivity.class);
                intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                GameListActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class getRekordTime extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            connectWebService = ConnectWebService.GetInstance();
            return connectWebService.getRekordTime(idGame, outdoorGameTime.getMacId());

        }

        @Override
        protected void onPostExecute(String jsonObject) {
            onCancelled();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
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
                    outdoorGame = gson.fromJson(game, OutdoorGame.class);
                    games.add(outdoorGame);
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
