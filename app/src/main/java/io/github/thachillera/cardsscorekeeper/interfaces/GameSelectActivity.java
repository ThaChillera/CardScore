package io.github.thachillera.cardsscorekeeper.interfaces;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.github.thachillera.cardsscorekeeper.R;
import io.github.thachillera.cardsscorekeeper.data.PersistenceManager;
import io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge.BoerenBridge;
import io.github.thachillera.cardsscorekeeper.interfaces.player.PlayerSelectActivity;

public class GameSelectActivity extends AppCompatActivity {

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
                    .setPositiveButton(R.string.ok, (dialog, which) -> startBoerenBridge())
                    .setNegativeButton(R.string.cancel, (dialog, which) -> startSelectPlayers());

            builder.create().show();
        } else {
            startSelectPlayers();
        }
    }

    private void startBoerenBridge() {
        Intent intent = new Intent(this, BoerenBridge.class);
        intent.putExtra(BoerenBridge.LOADSAVEGAMEEXTRA, true);
        startActivity(intent);
    }

    private void startSelectPlayers() {
        Intent intent = new Intent(this, PlayerSelectActivity.class);
        startActivity(intent);
    }
}
