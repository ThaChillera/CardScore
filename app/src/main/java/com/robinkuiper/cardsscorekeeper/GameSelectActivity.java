package com.robinkuiper.cardsscorekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.robinkuiper.cardsscorekeeper.data.PlayerData;

public class GameSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameselect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PlayerData.getInstance().loadPlayerData(this);
    }

    public void onGameSelect(View v) {

    }

    public void selectPlayers(View v) {
        Intent intent = new Intent(this, PlayerSelectActivity.class);
        startActivityForResult(intent, 0);
    }
}
