package io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge.rows;

import java.util.List;
import java.util.Map;

import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.ReadOnlyGameScoreManager;

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
        Map<Long, Integer> predictions = gameScoreManager.getPredictions(roundNumber);

        for (ScoreCard scoreCard : scoreCards) {
            if (predictions == null) {
                scoreCard.removePrediction();
            } else {
                scoreCard.setPrediction(predictions.get(scoreCard.getPlayerID()));
            }
        }

    }

    public void updateScores() {
        Map<Long, Integer> scores = gameScoreManager.getScores(roundNumber);

        for (ScoreCard scoreCard : scoreCards) {
            if (scores == null) {
                scoreCard.removeScore();
            } else {
                scoreCard.setScore(scores.get(scoreCard.getPlayerID()));
            }
        }

    }
}
