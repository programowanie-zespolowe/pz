package com.example.programowaniezespolowe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.example.programowaniezespolowe.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChooseActivity extends AppCompatActivity {

    private static int option = -1;
    private int idBuilding;
    private int idGame;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        Intent intent = getIntent();
        idGame = intent.getIntExtra("idGame", 0);
        idBuilding = intent.getIntExtra(ScanCode.BUILDING_ID, 0);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.wyborTrybu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        switch(keyCode) {
            case(KeyEvent.KEYCODE_BACK):
                Intent a1_intent = new Intent(this, ScanCode.class);
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
                Intent a1_intent = new Intent(this, ScanCode.class);
                startActivity(a1_intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        if(option == 0) {
            Intent intent = new Intent(ChooseActivity.this, GameListActivity.class);
            intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
            intent.putExtra("idGame", idGame);
            startActivity(intent);
        }else{
            Intent intent = new Intent(ChooseActivity.this, CategoryActivity.class);
            intent.putExtra(ScanCode.BUILDING_ID, idBuilding);
            startActivity(intent);
        }
    }

    public static int getOption() {
        return option;
    }
}
