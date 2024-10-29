package io.github.thachillera.cardsscorekeeper.data.game.boerenBridge;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.rounds.FinishedRound;
import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.rounds.PredictedRound;

public class GameScoreManager extends ReadOnlyGameScoreManager {

    public GameScoreManager(long[] selectedPlayers) {
        super(selectedPlayers);
    }

    /**
     * Loads game data and sets selected players in PlayerManager
     *
     * @param gameData
     * @return restored savegame
     */
    public static GameScoreManager loadGameData(String gameData) {
        //set local data
        GameScoreManager gameScoreManager = new Gson().fromJson(gameData, GameScoreManager.class);

        return gameScoreManager;
    }

    /**
     * enter values.
     * Will automatically assign as prediction or score, depending on game state
     *
     * @param values Tuple, key = playerID, value = result
     */
    public void enterValues(Map<Long, Integer> values) {
        switch (getNextEntryType()) {
            case PREDICTION:
                enterPredictions(values);
                break;
            case SCORE:
                enterScores(values);
                break;
        }
    }

    /**
     * enter predictions
     *
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
     *
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

    public void undo() {
        if (getLastEntryType() == EntryType.PREDICTION) {
            predictedRound = null;
        } else {
            FinishedRound lastRound = finishedRounds.get(finishedRounds.size() - 1);

            //remove last added score from finishedround
            finishedRounds.remove(lastRound);

            --round;
            //get old predicted round
            predictedRound = new PredictedRound(playerCount, getCardCount(round), lastRound.getPredictions());
        }
    }

    public String getSaveGameData() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.toJson(this);
    }

    public long[] getSelectedPlayers() {
        return selectedPlayers.clone();
    }
}
