package com.robinkuiper.cardsscorekeeper.data;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.robinkuiper.cardsscorekeeper.R;

public class EditPlayerActivity extends AppCompatActivity {
    PlayerManager playerManager = PlayerManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editplayer);
    }

    public void onClickDeletePlayer(View v) {

    }

    public void onClickSavePlayer(View v) {

    }
}
