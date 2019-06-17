package com.robinkuiper.cardsscorekeeper.interfaces.boerenBridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robinkuiper.cardsscorekeeper.R;
import com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.RoundScoreManager;

public class ScoreCard extends LinearLayout {
    final String TAG = "ScoreCard";

    TextView predictedRoundsView, scoredRoundsView;

    public ScoreCard(Context context, RoundScoreManager predictedRoundScoreManager, RoundScoreManager enteredRoundScoreManager) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boeren_bridge_scorecard, this);

        predictedRoundsView = findViewById(R.id.scorecard_boerenbridge_predictedrounds);
        scoredRoundsView = findViewById(R.id.scorecard_boerenbridge_scoredrounds);

        predictedRoundScoreManager.addTextView(predictedRoundsView);
        enteredRoundScoreManager.addTextView(scoredRoundsView);
    }

    public void setPrediction(int score) {
        predictedRoundsView.setText(Integer.toString(score));
    }

    public void setScore(int score) {
        scoredRoundsView.setText(Integer.toString(score));
    }
}
