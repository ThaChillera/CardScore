package com.robinkuiper.cardsscorekeeper.interfaces.boerenBridge.rows;

import com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.ReadOnlyGameScoreManager;

import java.util.List;
import java.util.Map;

public class RowManager {
    private final int roundNumber;
    private final List<ScoreCard> scoreCards;
    private final ReadOnlyGameScoreManager gameScoreManager;

    public RowManager(int roundNumber, List<ScoreCard> scoreCards, ReadOnlyGameScoreManager gameScoreManager) {
        this.roundNumber = roundNumber;
        this.scoreCards = scoreCards;
        this.gameScoreManager = gameScoreManager;
    }

    public void updatePredictions() {
        Map<Integer, Integer> predictions = gameScoreManager.getPredictions(roundNumber);

        for (ScoreCard scoreCard: scoreCards) {
            scoreCard.setPrediction(predictions.get(scoreCard.getPlayerID()));
        }
    }

    public void updateScores() {
        Map<Integer, Integer> scores = gameScoreManager.getScores(roundNumber);

        for (ScoreCard scoreCard: scoreCards) {
            scoreCard.setScore(scores.get(scoreCard.getPlayerID()));
        }
    }
}
