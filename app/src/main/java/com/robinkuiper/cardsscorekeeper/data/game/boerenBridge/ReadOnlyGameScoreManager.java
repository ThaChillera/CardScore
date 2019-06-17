package com.robinkuiper.cardsscorekeeper.data.game.boerenBridge;

import com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.rounds.FinishedRound;
import com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.rounds.PredictedRound;
import com.robinkuiper.cardsscorekeeper.data.players.PlayerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReadOnlyGameScoreManager {
    private final PlayerManager playerManager = PlayerManager.getInstance();

    //current round
    int round = 0;
    final int playerCount;

    final int maxCards;
    final int amountOfRounds;

    PredictedRound predictedRound = null;
    ArrayList<FinishedRound> finishedRounds = new ArrayList<>();

    ReadOnlyGameScoreManager(int playerCount) {
        this.playerCount = playerCount;

        maxCards = 52 % playerCount == 0 ? (52 - playerCount) / playerCount : 52 / playerCount;
        amountOfRounds = ((maxCards * 2)) - 1;
    }

    public int getRound() {
        return round;
    }

    public int getMaxCards() {
        return maxCards;
    }

    public int getAmountOfRounds() {
        return amountOfRounds;
    }

    public Map<Integer, Integer> getPredictions(int round) {
        if (round >= 0 && round < this.round) {
            return finishedRounds.get(round).getPredictions();
        } else if (round == this.round) {
            return predictedRound.getPredictions();
        } else {
            throw new IllegalArgumentException("invalid round");
        }
    }

    public Map<Integer, Integer> getScores(int round) {
        if (round >= 0 && round < this.round) {
            return finishedRounds.get(round).getScores();
        } else {
            throw new IllegalArgumentException("invalid round");
        }
    }

    /**
     * get Map of results
     * @return Map, key = playerID, value = result
     */
    public Map<Integer, Integer> getResults() {
        Map<Integer, Integer> results = new HashMap<>();

        for (int playerID: playerManager.getSelectedPlayers()) {
            results.put(playerID, getResult(playerID));
        }

        return Collections.unmodifiableMap(results);
    }

    /**
     * get result of given player ID
     * @param playerID ID of player
     * @return result
     */
    public int getResult(int playerID) {
        int result = 0;
        for (FinishedRound round: finishedRounds) {
            result += round.getResults().get(playerID);
        }

        return result;
    }

    public enum EntryType {
        PREDICTION, SCORE
    }

    public GameScoreManager.EntryType getNextEntry() {
        return predictedRound == null ? GameScoreManager.EntryType.PREDICTION : GameScoreManager.EntryType.SCORE;
    }
}
