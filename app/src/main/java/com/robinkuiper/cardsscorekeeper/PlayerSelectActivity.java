package com.robinkuiper.cardsscorekeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.robinkuiper.cardsscorekeeper.data.EditPlayerActivity;
import com.robinkuiper.cardsscorekeeper.data.PlayerManager;

public class PlayerSelectActivity extends AppCompatActivity {
    PlayerManager playerManager = PlayerManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout linearLayout = findViewById(R.id.content_playerselect_linearlayout);
        LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

        for (int i = 0; i < playerManager.getPlayerCount(); i++) {
            CheckBox button = new CheckBox(getApplicationContext());
            button.setChecked(playerManager.isPlayerSelected(i));
            button.setText(playerManager.getPlayerName(i));
            button.setLayoutParams(checkBoxParams);
            button.setOnClickListener(new CheckBoxOnClickListener(i));
            button.setOnLongClickListener(new CheckBoxOnLongClickListener(i, this));

            linearLayout.addView(button);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClickReturnClick(View v) {
        finish();
    }

    public void onClickCreatePlayer(View v) {
        Intent intent = new Intent(this, EditPlayerActivity.class);
        startActivity(intent);
    }

    private class CheckBoxOnClickListener implements View.OnClickListener {
        final int ID;

        public CheckBoxOnClickListener(int ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View v) {
            CheckBox box = (CheckBox) v;
            if (box.isChecked()) {
                playerManager.selectPlayer(ID);
            } else {
                playerManager.deselectPlayer(ID);
            }
        }
    }

    private class CheckBoxOnLongClickListener implements View.OnLongClickListener {
        final int ID;
        final Activity ACTIVITY;

        public CheckBoxOnLongClickListener(int ID, Activity ACTIVITY) {
            this.ID = ID;
            this.ACTIVITY = ACTIVITY;
        }

        @Override
        public boolean onLongClick(View v) {
            Intent intent = new Intent(ACTIVITY, EditPlayerActivity.class);
            intent.putExtra(EditPlayerActivity.PLAYERIDEXTRA, ID);
            startActivity(intent);
            return true;
        }
    }
}
