package io.github.thachillera.cardsscorekeeper.interfaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import io.github.thachillera.cardsscorekeeper.R;
import io.github.thachillera.cardsscorekeeper.data.PersistenceManager;
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
        toolbar.setTitle(R.string.title_activity_game_select);
        setSupportActionBar(toolbar);

        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        persistenceManager.setContext(getBaseContext());
        persistenceManager.loadPlayerData();
    }

    public void onGameSelect(View v) {
        if (PersistenceManager.getInstance().hasExistingSavedGame()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.savegame_load_request_body)
                    .setTitle(R.string.savegame_load_request_title)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startBoerenBridge(true);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startBoerenBridge(false);
                        }
                    });

            builder.create().show();
        } else {
            startBoerenBridge();
        }
    }

    private void startBoerenBridge() {
        startBoerenBridge(false);
    }

    private void startBoerenBridge(boolean loadSave) {
        if (loadSave) {
            Intent intent = new Intent(this, BoerenBridge.class);
            intent.putExtra(BoerenBridge.LOADSAVEGAMEEXTRA, true);
            startActivity(intent);
        } else if (playerManager.getSelectedPlayerCount() > 1) {
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
