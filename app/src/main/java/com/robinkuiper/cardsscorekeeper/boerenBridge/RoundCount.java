package com.robinkuiper.cardsscorekeeper.boerenBridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robinkuiper.cardsscorekeeper.R;

public class RoundCount extends RelativeLayout {

    public RoundCount(Context context, int roundNumber, int cardCount) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boeren_bridge_roundcount, this);

        TextView roundNumberView = findViewById(R.id.roundcount_boerenbridge_roundnumber);
        roundNumberView.setText(Integer.toString(roundNumber));

        TextView cardCountView = findViewById(R.id.roundcount_boerenbridge_cardcount);
        cardCountView.setText(Integer.toString(cardCount));
    }


}
