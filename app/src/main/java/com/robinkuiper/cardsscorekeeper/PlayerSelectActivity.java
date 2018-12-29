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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.robinkuiper.cardsscorekeeper.data.PlayerData;

public class PlayerSelectActivity extends AppCompatActivity {
    PlayerData playerData = PlayerData.getInstance();

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

        for (int i = 0; i < playerData.getPlayerCount(); i++) {
            CheckBox button = new CheckBox(getApplicationContext());
            button.setText(playerData.getPlayerName(i));
            button.setLayoutParams(checkBoxParams);
            button.setOnClickListener(new CheckBoxOnClickListener(i));

            linearLayout.addView(button);
        }
    }

    private class CheckBoxOnClickListener implements View.OnClickListener {
        final int ID;

        public CheckBoxOnClickListener(int ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View v) {
            CheckBox box = (CheckBox) v;
            playerData.selectPlayer(ID, box.isChecked());
        }
    }

    public void onReturnClick(View v) {
        finish();
    }
}
