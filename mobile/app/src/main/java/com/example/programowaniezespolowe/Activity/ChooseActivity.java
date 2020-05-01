package com.example.programowaniezespolowe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.example.programowaniezespolowe.R;
import androidx.appcompat.app.AppCompatActivity;

public class ChooseActivity extends AppCompatActivity {

    private static int option = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    public void chooseGame(View view) {
        option = 0;
        start();
    }

    public void chooseNavigation(View view) {
        option = 1;
        start();
    }

    private void start(){
        Intent intent = new Intent(ChooseActivity.this, ScanCode.class);
        startActivity(intent);
    }

    public static int getOption() {
        return option;
    }
}
