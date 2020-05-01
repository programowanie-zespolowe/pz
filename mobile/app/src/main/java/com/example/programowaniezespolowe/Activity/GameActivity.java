package com.example.programowaniezespolowe.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.programowaniezespolowe.Connection.ConnectWebService;
import com.example.programowaniezespolowe.Data.OutdoorGameHints;
import com.example.programowaniezespolowe.Data.OutdoorGamePath;
import com.example.programowaniezespolowe.Data.OutdoorGameTime;
import com.example.programowaniezespolowe.Data.PointPath;
import com.example.programowaniezespolowe.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
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

    private TextView textView;
    private TextView showHint;
    private EditText editText;
    private  OutdoorGameTime outdoorGameTime;
    private String test2;

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
        textView = findViewById(R.id.question);
        textView.setText(question);
        editText = findViewById(R.id.answer);
        showHint = findViewById(R.id.hint);
        outdoorGameTime = OutdoorGameTime.getInstance();
        if(hint != null){
            showHint.setText(hint);
        }
        if(idNextPoint == -1) {
            new getFirstPoint().execute();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameActivity.this, GameListActivity.class);
        intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
        //intent.putExtra("idGame", idGame);
        startActivity(intent);
    }

    public void checkAnswer(View view) {
           String odp = editText.getText().toString();
           if (odp.toLowerCase().contains(answer.toLowerCase())) {
               Toast.makeText(getApplicationContext(), "BRAWO!!!", Toast.LENGTH_SHORT).show();
               if (idNextPoint == 0) {
                   AsyncTask asyncTask = new getTime().execute();
                   String s = null;
                   try {
                       s = (String) asyncTask.get();
                       GsonBuilder gsonBuilder = new GsonBuilder();
                       gsonBuilder.setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                       Gson gson = gsonBuilder.create();
                       Date date = gson.fromJson(s, Date.class);
                       outdoorGameTime.setEnd(date);
                   } catch (ExecutionException e) {
                       e.printStackTrace();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   long czas = outdoorGameTime.getEnd().getTime() - outdoorGameTime.getStart().getTime();
                   long diffSeconds = czas / 1000 % 60;
                   long diffMinutes = czas / (60 * 1000) % 60;
                   long diffHours = czas / (60 * 60 * 1000);
                   Toast.makeText(getApplicationContext(), "Koniec!!!", Toast.LENGTH_SHORT).show();
                   Toast.makeText(getApplicationContext(), "Twój czas: " + diffHours + ":" + diffMinutes + ":" + diffSeconds, Toast.LENGTH_LONG).show();
               } else {
                   new getFirstPoint().execute();
               }

           } else {
               Toast.makeText(getApplicationContext(), "ŹLE!!!", Toast.LENGTH_SHORT).show();
           }
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
            return connectWebService.getOutdoorTime(idGame, outdoorGameTime.getName(), outdoorGameTime.getMacId(), true);
        }

    }


    private class getFirstPoint extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            connectWebService = ConnectWebService.GetInstance();
            return connectWebService.getFirstGamePoint(idGame, idNextPoint );
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            Gson gson = new Gson();
            String next = null;
            for(int i = 0; i < jsonArray.length(); i++){
                try {
                    next = jsonArray.getString(i);
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    idPoint = (int) jsonObject.get("idPoint");
                    outdoorGamePath = gson.fromJson(next, OutdoorGamePath.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            pointPath = PointPath.getInstance();
            pointPath.setTargetPoint(idPoint);
            if(outdoorGamePath.getIdHintPoint() != null){
                hintPoint = outdoorGamePath.getIdHintPoint();
            }
            if(outdoorGamePath.getIdNextPoint() != null) {
                idNextPoint = outdoorGamePath.getIdNextPoint();
            }else{
                idNextPoint = 0;
            }
            if(pointPath.getCurrentPoint() == idPoint){
                answer = outdoorGamePath.getAnswer();
                textView.setText(outdoorGamePath.getQuestion());
            }else {
                Intent intent = new Intent(GameActivity.this, Navigation_activity.class);
                intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
                intent.putExtra("idGame", idGame);
                intent.putExtra("idNextPoint", idNextPoint);
                intent.putExtra("question", outdoorGamePath.getQuestion());
                intent.putExtra("answer", outdoorGamePath.getAnswer());
                intent.putExtra("hintPoint", hintPoint);
                startActivity(intent);
            }
        }
    }
}
