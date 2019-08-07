package com.robinkuiper.cardsscorekeeper.data.game.boerenBridge;

import com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.rounds.PredictedRound;

import java.util.Map;

public class GameScoreManager extends ReadOnlyGameScoreManager {

    public GameScoreManager(int playerCount) {
        super(playerCount);
    }

    /**
     * enter values.
     * Will automatically assign as prediction or score, depending on game state
     * @param values Tuple, key = playerID, value = result
     */
    public void enterValues(Map<Long, Integer> values) {
        switch (getNextEntry()) {
            case PREDICTION:
                enterPredictions(values);
            case SCORE:
                enterScores(values);
        }
    }

    /**
     * enter predictions
     * @param predictions Tuple, key = playerID, value = result
     */
    public void enterPredictions(Map<Long, Integer> predictions) {
        if (predictedRound != null || finishedRounds.size() != round) {
            //didn't finalise previous round data entry
            throw new IllegalStateException("previous round not complete");
        }

        predictedRound = new PredictedRound(playerCount, getCardCount(round), predictions);
    }

    /**
     * enter scores
     * @param scores Tuple, key = playerID, value = result
     */
    public void enterScores(Map<Long, Integer> scores) {
        if (predictedRound == null) {
            throw new IllegalStateException("Missing Predictions");
        }

        finishedRounds.add(predictedRound.addScores(scores));

        ++round;
        predictedRound = null;
    }
}
