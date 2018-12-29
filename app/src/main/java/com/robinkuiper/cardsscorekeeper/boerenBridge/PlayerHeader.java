package com.robinkuiper.cardsscorekeeper.boerenBridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robinkuiper.cardsscorekeeper.R;

public class PlayerHeader extends RelativeLayout {
    TextView playerScoreView;

    public PlayerHeader(Context context, String name) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boeren_bridge_playerheader, this);

        TextView playerNameView = findViewById(R.id.playerheader_boerenbridge_name);
        playerNameView.setText(name);

        playerScoreView = findViewById(R.id.playerheader_boerenbridge_playerscore);
        playerScoreView.setText(Integer.toString(0));
    }

    void setPlayerScoreView(int score) {
        playerScoreView.setText(Integer.toString(score));
    }
}
