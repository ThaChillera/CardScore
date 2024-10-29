package io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge.rows;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.thachillera.cardsscorekeeper.R;

/**
 * Squares in the score sheet.
 * Displays prediction and score for given player, in a specific round
 */
public class ScoreCard extends LinearLayout {
    final String TAG = "ScoreCard";
    private final long playerID;

    TextView predictedRoundsView, scoredRoundsView;

    public ScoreCard(Context context, long playerID) {
        super(context);
        this.playerID = playerID;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boeren_bridge_scorecard, this);

        predictedRoundsView = findViewById(R.id.scorecard_boerenbridge_predictedrounds);
        scoredRoundsView = findViewById(R.id.scorecard_boerenbridge_scoredrounds);
    }

    public long getPlayerID() {
        return playerID;
    }

    void setPrediction(int score) {
        predictedRoundsView.setText(Integer.toString(score));
    }

    void removePrediction() {
        predictedRoundsView.setText(null);
    }

    void setScore(int score) {
        scoredRoundsView.setText(Integer.toString(score));
    }

    void removeScore() {
        scoredRoundsView.setText(null);
    }
}
