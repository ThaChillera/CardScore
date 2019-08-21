package io.github.thachillera.cardsscorekeeper.data.game.boerenBridge;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.rounds.FinishedRound;
import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.rounds.PredictedRound;
import io.github.thachillera.cardsscorekeeper.data.players.PlayerManager;

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

    /**
     * get card count for given round
     * @param round zero-based round number
     * @return card count
     */
    public int getCardCount(int round) {
        round += 1;
        return round <= maxCards ? round : maxCards - (round - maxCards);
    }

    public Map<Long, Integer> getPredictions(int round) {
        if (round >= 0 && round < this.round) {
            return finishedRounds.get(round).getPredictions();
        } else if (round == this.round) {
            return predictedRound.getPredictions();
        } else {
            throw new IllegalArgumentException("invalid round");
        }
    }

    public Map<Long, Integer> getScores(int round) {
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
    public Map<Long, Integer> getResults() {
        Map<Long, Integer> results = new HashMap<>();

        for (long playerID: playerManager.getSelectedPlayers()) {
            results.put(playerID, getResult(playerID));
        }

        return Collections.unmodifiableMap(results);
    }

    /**
     * get result of given player ID
     * @param playerID ID of player
     * @return result
     */
    public int getResult(long playerID) {
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
