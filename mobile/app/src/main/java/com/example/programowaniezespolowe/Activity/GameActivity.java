package com.example.programowaniezespolowe.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.programowaniezespolowe.Connection.ConnectWebService;
import com.example.programowaniezespolowe.Data.FinishGame;
import com.example.programowaniezespolowe.Data.OutdoorGameHints;
import com.example.programowaniezespolowe.Data.OutdoorGamePath;
import com.example.programowaniezespolowe.Data.OutdoorGameTime;
import com.example.programowaniezespolowe.Data.PointPath;
import com.example.programowaniezespolowe.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GameActivity extends AppCompatActivity {
    private int idGame;
    private String question;
    private String answer;
    private int idNextPoint;
    private int idBuilding;
    private OutdoorGameHints outdoorGameHints;
    private OutdoorGamePath outdoorGamePath;
    private ConnectWebService connectWebService;
    private PointPath pointPath;
    private int idPoint;
    private int hintPoint;
    private String hint;
    private ImageButton hintButton;
    private TextView textViewQuestion;
    private TextView showHint;
    private TextView hintText;
    private EditText editTextAnswer;
    private  OutdoorGameTime outdoorGameTime;
    private Toolbar toolbar;
    public static String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        idNextPoint = intent.getIntExtra("idNextPoint", -1);
        question = intent.getStringExtra("question");
        answer = intent.getStringExtra("answer");
        idGame = intent.getIntExtra("idGame", 0);
        idBuilding = intent.getIntExtra(ScanCode.BUILDING_ID, 0);
        hintPoint = intent.getIntExtra("hintPoint", 0);
        hint = intent.getStringExtra("hint");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(gameName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hintText = findViewById(R.id.hintText);
        hintText.setVisibility(View.INVISIBLE);
        textViewQuestion = findViewById(R.id.question);
        textViewQuestion.setText(question);
        editTextAnswer = findViewById(R.id.answer);
        showHint = findViewById(R.id.hint);
        outdoorGameTime = OutdoorGameTime.getInstance();
        hintButton = findViewById(R.id.cheat);
        if(hint != null){
            hintText.setVisibility(View.VISIBLE);
            hintButton.setVisibility(View.INVISIBLE);
            showHint.setText(hint);
        }
        if(idNextPoint == -1) {
            try {
                getGamePoint();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(hintPoint == 0){
            hintButton.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case (KeyEvent.KEYCODE_BACK):
                Intent intent = new Intent(GameActivity.this, GameListActivity.class);
                intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                GameActivity.this.finish();
                return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent = new Intent(GameActivity.this, GameListActivity.class);
                intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                GameActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkAnswer(View view) {
           String odp = editTextAnswer.getText().toString();
           if (odp.toLowerCase().contains(answer.toLowerCase())) {
               Toast.makeText(getApplicationContext(), "BRAWO!!!", Toast.LENGTH_SHORT).show();
               if (idNextPoint == 0) {
                   AsyncTask asyncTask = new getTime().execute();
                   String s = null;
                   try {
                       s = (String) asyncTask.get();
                   } catch (ExecutionException e) {
                       e.printStackTrace();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   double czasDouble = Double.parseDouble(s);
                   int czas = (int)czasDouble;
                   FinishGame finishGame = new FinishGame();
                   finishGame.setIdGame(idGame);
                   finishGame.setLiczbaSekund(czas);
                   addFinishGameToSharedPreferences(finishGame);
                   long diffSeconds = czas % 60;
                   long diffMinutes = (czas % 3600) / 60;
                   long diffHours = czas / 3600;
                   Toast.makeText(getApplicationContext(), "Koniec!!!", Toast.LENGTH_SHORT).show();
                   String napis = String.format("Twój czas:  %02d:%02d:%02d",diffHours, diffMinutes, diffSeconds);
                   Intent intent = new Intent(GameActivity.this, RankingActivity.class);
                   intent.putExtra("czas", napis);
                   intent.putExtra("idGame", idGame);
                   intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
                   startActivity(intent);
               }
               else {
                   try {
                       getGamePoint();
                   } catch (ExecutionException e) {
                       e.printStackTrace();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }

           } else {
               Toast.makeText(getApplicationContext(), "ŹLE!!!", Toast.LENGTH_SHORT).show();
           }
       }

    private void addFinishGameToSharedPreferences(FinishGame finishGame) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String finishGames = sharedPreferences.getString("finishGames", null);
        Gson gson = new Gson();
        if(finishGames == null){
            List<FinishGame> finishGameList = new ArrayList<FinishGame>();
            finishGameList.add(finishGame);
            editor.putString( "finishGames",gson.toJson(finishGameList));
            editor.apply();
            return;
        }
        FinishGame[] fg =  gson.fromJson(finishGames, FinishGame[].class);
        List<FinishGame> finishGameList = new ArrayList<FinishGame>(Arrays.asList(fg));
        finishGameList.add(finishGame);
        editor.putString( "finishGames",gson.toJson(finishGameList));
        editor.apply();
    }


    public void podpowiedz(View view) {
        if(hintPoint != 0){
            new getHint().execute();
        }
    }

    private class getHint extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            connectWebService = ConnectWebService.GetInstance();
            return connectWebService.getHintGameP(idGame, hintPoint);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            Gson gson = new Gson();
            String next = null;
            for(int i = 0; i < jsonArray.length(); i++){
                try {
                    next = jsonArray.getString(i);
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    outdoorGameHints = gson.fromJson(next, OutdoorGameHints.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            pointPath = PointPath.getInstance();
            pointPath.setTargetPoint(outdoorGameHints.getIdPoint());
            Intent intent = new Intent(GameActivity.this, Navigation_activity.class);
            intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
            intent.putExtra("idGame", idGame);
            intent.putExtra("idNextPoint", idNextPoint);
            intent.putExtra("question", question);
            intent.putExtra("answer", answer);
            intent.putExtra("hintPoint", outdoorGameHints.getIdPoint());
            intent.putExtra("hint", outdoorGameHints.getHint());
            startActivity(intent);
        }
    }
    private class getTime extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            connectWebService = ConnectWebService.GetInstance();
            return connectWebService.getOutdoorTime(idGame, outdoorGameTime.getName(), outdoorGameTime.getMacId(), false);
        }
    }
    private class getFirstPoint extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            connectWebService = ConnectWebService.GetInstance();
            return connectWebService.getFirstGamePoint(idGame, idNextPoint );
        }
    }

    private void getGamePoint() throws ExecutionException, InterruptedException {
        AsyncTask jsonArray = new getFirstPoint().execute();
        JSONArray jsonArray1 = (JSONArray) jsonArray.get();
        Gson gson = new Gson();
        String next = null;
        for(int i = 0; i < jsonArray1.length(); i++){
            try {
                next = jsonArray1.getString(i);
                JSONObject jsonObject = (JSONObject) jsonArray1.get(i);
                idPoint = (int) jsonObject.get("idPoint");
                outdoorGamePath = gson.fromJson(next, OutdoorGamePath.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        pointPath = PointPath.getInstance();
        pointPath.setTargetPoint(idPoint);
        if(outdoorGamePath.getIdHintPoint() != null) {
            hintPoint = outdoorGamePath.getIdHintPoint();
        }else{
            hintPoint = 0;
        }
        if(outdoorGamePath.getIdNextPoint() != null) {
            idNextPoint = outdoorGamePath.getIdNextPoint();
        }else{
            idNextPoint = 0;
        }
        if(pointPath.getCurrentPoint() == idPoint){
            answer = outdoorGamePath.getAnswer();
            textViewQuestion.setText(outdoorGamePath.getQuestion());
        }else {
            Intent intent = new Intent(GameActivity.this, Navigation_activity.class);
            intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
            intent.putExtra("idGame", idGame);
            intent.putExtra("idNextPoint", idNextPoint);
            intent.putExtra("question", outdoorGamePath.getQuestion());
            intent.putExtra("answer", outdoorGamePath.getAnswer());
            intent.putExtra("hintPoint", outdoorGamePath.getIdHintPoint());
            startActivity(intent);
        }
    }


}
