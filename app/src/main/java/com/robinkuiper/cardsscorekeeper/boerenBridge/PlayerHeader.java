package com.robinkuiper.cardsscorekeeper.boerenBridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robinkuiper.cardsscorekeeper.R;

public class PlayerHeader extends RelativeLayout {
    TextView playerScoreView;

    public PlayerHeader(Context context, String name, int number) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boeren_bridge_playerheader, this);

        TextView playerNumberView = findViewById(R.id.playerheader_boerenbridge_playernumber);
        playerNumberView.setText(Integer.toString(number));

        TextView playerNameView = findViewById(R.id.playerheader_boerenbridge_name);
        playerNameView.setText(name);

        playerScoreView = findViewById(R.id.playerheader_boerenbridge_playerscore);
        playerScoreView.setText(Integer.toString(0));
    }

    void setPlayerScoreView(int score) {
        playerScoreView.setText(Integer.toString(score));
    }
}
