package com.robinkuiper.cardsscorekeeper.interfaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.robinkuiper.cardsscorekeeper.R;
import com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.ReadOnlyGameScoreManager;
import com.robinkuiper.cardsscorekeeper.interfaces.boerenBridge.BoerenBridge;
import com.robinkuiper.cardsscorekeeper.data.players.PlayerManager;
import com.robinkuiper.cardsscorekeeper.interfaces.player.PlayerSelectActivity;

public class GameSelectActivity extends AppCompatActivity {
    PlayerManager playerManager = PlayerManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameselect);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        playerManager.loadPlayerData(this);
    }

    public void onGameSelect(View v) {
        if (ReadOnlyGameScoreManager.hasExistingSave(getApplicationContext())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.savegame_load_request_body)
                    .setTitle(R.string.savegame_load_request_title);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), BoerenBridge.class);
                    intent.putExtra(BoerenBridge.LOADSAVEGAMEEXTRA, true);
                    startActivity(intent);
                }
            })
                    .setNegativeButton(R.string.cancel, null);

            builder.create().show();
        } else {
            if (playerManager.getSelectedPlayerCount() > 1) {
                Intent intent = new Intent(this, BoerenBridge.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, getString(R.string.game_select_insufficient_players), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void selectPlayers(View v) {
        Intent intent = new Intent(this, PlayerSelectActivity.class);
        startActivity(intent);
    }
}
