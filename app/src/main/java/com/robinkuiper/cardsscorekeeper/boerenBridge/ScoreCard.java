package com.robinkuiper.cardsscorekeeper.boerenBridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robinkuiper.cardsscorekeeper.R;

public class ScoreCard extends LinearLayout {
    final String TAG = "ScoreCard";

    TextView predictedRoundsView, scoredRoundsView;

    public ScoreCard(Context context, BoerenBridge.ScoreManager predictedScoreManager, BoerenBridge.ScoreManager enteredScoreManager) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boeren_bridge_scorecard, this);

        predictedRoundsView = findViewById(R.id.scorecard_boerenbridge_predictedrounds);
        scoredRoundsView = findViewById(R.id.scorecard_boerenbridge_scoredrounds);

        predictedScoreManager.addTextView(predictedRoundsView);
        enteredScoreManager.addTextView(scoredRoundsView);
    }

    public void setPrediction(int score) {
        predictedRoundsView.setText(Integer.toString(score));
    }

    public void setScore(int score) {
        scoredRoundsView.setText(Integer.toString(score));
    }
}
