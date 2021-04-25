package io.github.thachillera.cardsscorekeeper.interfaces.player;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.github.thachillera.cardsscorekeeper.R;
import io.github.thachillera.cardsscorekeeper.data.players.PlayerManager;

public class EditPlayerActivity extends AppCompatActivity {
    public static final String PLAYERIDEXTRA = "PLAYERID";

    PlayerManager playerManager = PlayerManager.getInstance();

    private long playerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editplayer);

        playerId = getIntent().getLongExtra(PLAYERIDEXTRA, -1);

        if (playerId == -1) {
            //new player
            Button button = findViewById(R.id.editPlayer_deletePlayer);
            button.setVisibility(Button.INVISIBLE);
        } else {
            //existing player
            EditText name = findViewById(R.id.editPlayer_editText_name);
            EditText shortName = findViewById(R.id.editPlayer_editText_shortname);

            name.setText(playerManager.getPlayerName(playerId));
            shortName.setText(playerManager.getPlayerShortName(playerId));
        }
    }

    public void onClickDeletePlayer(View v) {
        playerManager.deletePlayer(playerId);
        finish();
    }

    public void onClickSavePlayer(View v) {
        EditText name = findViewById(R.id.editPlayer_editText_name);
        EditText shortName = findViewById(R.id.editPlayer_editText_shortname);

        if (name.getText().length() == 0) {
            //no name
            Toast.makeText(this, getResources().getString(R.string.editPlayer_missing_name), Toast.LENGTH_SHORT).show();
        } else if (shortName.getText().length() == 0) {
            //no shortname
            Toast.makeText(this, getResources().getString(R.string.editPlayer_missing_shortname), Toast.LENGTH_SHORT).show();
        } else if (shortName.getText().length() > 3) {
            //shortname too long
            Toast.makeText(this, R.string.editPlayer_too_long_shortname, Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (playerId == -1) {
                    //save & finish
                    playerManager.addPlayer(name.getText().toString(), shortName.getText().toString());
                } else {
                    playerManager.editPlayer(playerId, name.getText().toString(), shortName.getText().toString());
                }
                finish();
            } catch (IllegalArgumentException e) {
                //Illegal argument, fail
                Toast.makeText(this, R.string.editPlayer_input_error, Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
}
