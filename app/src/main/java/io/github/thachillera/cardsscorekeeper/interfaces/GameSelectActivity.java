package io.github.thachillera.cardsscorekeeper.interfaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import io.github.thachillera.cardsscorekeeper.R;
import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.ReadOnlyGameScoreManager;
import io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge.BoerenBridge;
import io.github.thachillera.cardsscorekeeper.data.players.PlayerManager;
import io.github.thachillera.cardsscorekeeper.interfaces.player.PlayerSelectActivity;

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
        if (playerManager.getSelectedPlayerCount() > 1) {
            Intent intent = new Intent(this, BoerenBridge.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.game_select_insufficient_players), Toast.LENGTH_LONG).show();
        }
    }

    public void selectPlayers(View v) {
        Intent intent = new Intent(this, PlayerSelectActivity.class);
        startActivity(intent);
    }
}
