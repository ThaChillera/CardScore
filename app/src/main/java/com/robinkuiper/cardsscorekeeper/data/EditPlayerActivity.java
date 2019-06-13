package com.robinkuiper.cardsscorekeeper.data;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.robinkuiper.cardsscorekeeper.R;

public class EditPlayerActivity extends AppCompatActivity {
    public static final String PLAYERIDEXTRA = "PLAYERID";

    PlayerManager playerManager = PlayerManager.getInstance();

    int playerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editplayer);

        playerId = getIntent().getIntExtra(PLAYERIDEXTRA, -1);

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
            Toast.makeText(this, "Shortname may only be maximum 3 characters", Toast.LENGTH_SHORT).show();
        } else {
            if (playerId == -1) {
                //save & finish
                playerManager.addPlayer(name.getText().toString(), shortName.getText().toString());
            } else {
                playerManager.editPlayer(playerId, name.getText().toString(), shortName.getText().toString());
            }
            finish();
        }
    }
}
