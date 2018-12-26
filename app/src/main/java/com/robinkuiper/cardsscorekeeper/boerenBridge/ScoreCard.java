package com.robinkuiper.cardsscorekeeper.boerenBridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robinkuiper.cardsscorekeeper.R;

public class ScoreCard extends LinearLayout {
    TextView predictedRoundsView, scoredRoundsView;
    Button predictRoundsView, scoreRoundsView;

    public ScoreCard(final Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boeren_bridge_scorecard, this);

        predictedRoundsView = findViewById(R.id.scorecard_boerenbridge_predictedrounds);
        scoredRoundsView = findViewById(R.id.scorecard_boerenbridge_scoredrounds);

        predictRoundsView = findViewById(R.id.scorecard_boerenbridge_predictrounds_button);
        scoreRoundsView = findViewById(R.id.scorecard_boerenbridge_scorerounds_button);

        predictedRoundsView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
