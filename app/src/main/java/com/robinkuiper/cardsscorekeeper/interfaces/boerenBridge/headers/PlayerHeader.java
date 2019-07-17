package com.robinkuiper.cardsscorekeeper.interfaces.boerenBridge.headers;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robinkuiper.cardsscorekeeper.R;

/**
 * Headers of the score sheet.
 * Displays the player name & total score
 */
public class PlayerHeader extends RelativeLayout {
    TextView playerScoreView;
    private final long playerID;

    public PlayerHeader(Context context, String name, long playerID) {
        super(context);
        this.playerID = playerID;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boeren_bridge_playerheader, this);

        TextView playerNameView = findViewById(R.id.playerheader_boerenbridge_name);
        playerNameView.setText(name);

        playerScoreView = findViewById(R.id.playerheader_boerenbridge_playerscore);
        playerScoreView.setText(Integer.toString(0));
    }

    public long getPlayerID() {
        return playerID;
    }

    void setPlayerScoreView(int score) {
        playerScoreView.setText(Integer.toString(score));
    }
}
