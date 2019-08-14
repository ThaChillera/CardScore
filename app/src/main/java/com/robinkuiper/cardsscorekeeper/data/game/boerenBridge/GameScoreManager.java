package com.robinkuiper.cardsscorekeeper.data.game.boerenBridge;


import android.content.Context;

import com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.rounds.FinishedRound;
import com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.rounds.PredictedRound;

import java.util.Map;

public class GameScoreManager extends ReadOnlyGameScoreManager {

    public GameScoreManager(final Context context, int playerCount) {
        super(context, playerCount);
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
        saveGameData();
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
        saveGameData();
    }

    public void undo() {
        if (getLastEntry() == EntryType.PREDICTION) {
            predictedRound = null;
        } else {
            FinishedRound lastRound = finishedRounds.get(finishedRounds.size() - 1);

            //remove last added score from finishedround
            finishedRounds.remove(lastRound);

            --round;
            //get old predicted round
            predictedRound = new PredictedRound(playerCount, getCardCount(round), lastRound.getPredictions());
        }

        saveGameData();
    }
}
