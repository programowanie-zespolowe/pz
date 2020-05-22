package com.example.programowaniezespolowe.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.programowaniezespolowe.Connection.ConnectWebService;
import com.example.programowaniezespolowe.Data.Building;
import com.example.programowaniezespolowe.Data.Device;
import com.example.programowaniezespolowe.Data.OutdoorGameTime;
import com.example.programowaniezespolowe.Data.PointPath;
import com.example.programowaniezespolowe.R;
import com.google.gson.Gson;
import com.google.zxing.Result;
import org.json.JSONArray;
import org.json.JSONException;
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
//        o.setMacId("13:05:19:41:17:00");
        connect = ConnectWebService.GetInstance();
        connect.setDevice(device);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
        }

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
        intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
        intent.putExtra("idGame", idGame);
        startActivity(intent);
    }
}
