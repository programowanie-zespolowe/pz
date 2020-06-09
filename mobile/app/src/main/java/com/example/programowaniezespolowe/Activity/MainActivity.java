package com.example.programowaniezespolowe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.programowaniezespolowe.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void start(View view) {
        Intent intent = new Intent(MainActivity.this, ScanCode.class);
        startActivity(intent);
    }
}
