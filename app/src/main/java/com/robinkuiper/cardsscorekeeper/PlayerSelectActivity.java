package com.robinkuiper.cardsscorekeeper;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.robinkuiper.cardsscorekeeper.data.PlayerManager;

public class PlayerSelectActivity extends AppCompatActivity {
    PlayerManager playerManager = PlayerManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.playerselect_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        LinearLayout linearLayout = findViewById(R.id.content_playerselect_linearlayout);
        LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

        for (int i = 0; i < playerManager.getPlayerCount(); i++) {
            CheckBox button = new CheckBox(getApplicationContext());
            button.setText(playerManager.getPlayerName(i));
            button.setLayoutParams(checkBoxParams);
            button.setOnClickListener(new CheckBoxOnClickListener(i));
            button.setOnLongClickListener(new CheckBoxOnLongClickListener());

            linearLayout.addView(button);
        }
    }

    public void onClickReturnClick(View v) {
        finish();
    }

    public void onClickCreatePlayer(View v) {
        //person editing view
        //
    }

    private class CheckBoxOnClickListener implements View.OnClickListener {
        final int ID;

        public CheckBoxOnClickListener(int ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View v) {
            CheckBox box = (CheckBox) v;
            playerManager.selectPlayer(ID, box.isChecked());
        }
    }

    private class CheckBoxOnLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(getApplicationContext(), "onHold", Toast.LENGTH_LONG).show();
            return true;
        }
    }
}
