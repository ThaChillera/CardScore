package io.github.thachillera.cardsscorekeeper.data.game.boerenBridge;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.thachillera.cardsscorekeeper.data.PersistenceManager;
import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.rounds.FinishedRound;
import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.rounds.PredictedRound;
import io.github.thachillera.cardsscorekeeper.data.players.PlayerManager;

import java.util.Arrays;
import java.util.Map;

public class GameScoreManager extends ReadOnlyGameScoreManager {

    public GameScoreManager(long[] selectedPlayers) {
        super(selectedPlayers);
    }

    /**
     * enter values.
     * Will automatically assign as prediction or score, depending on game state
     * @param values Tuple, key = playerID, value = result
     */
    public void enterValues(Map<Long, Integer> values) {
        switch (getNextEntryType()) {
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
        PersistenceManager.getInstance().saveGame(this);
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
        PersistenceManager.getInstance().saveGame(this);
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

        PersistenceManager.getInstance().saveGame(this);
    }

    public byte[] getSaveGameData() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.toJson(this).getBytes();
    }

    /**
     * Loads game data and sets selected players in PlayerManager
     * @param gameData
     * @return restored savegame
     */
    public static GameScoreManager loadGameData(byte[] gameData) {
        //set local data
        GameScoreManager gameScoreManager = new Gson().fromJson(new String(gameData), GameScoreManager.class);
        //set selected players
        PlayerManager.getInstance().replaceSelectedPlayers(gameScoreManager.selectedPlayers);
        return gameScoreManager;
    }

}
