package io.github.thachillera.cardsscorekeeper.data.game.boerenBridge;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.rounds.FinishedRound;
import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.rounds.PredictedRound;

public abstract class ReadOnlyGameScoreManager {

    final int playerCount;
    final long[] selectedPlayers;
    private final int maxCards, amountOfRounds;

    //current round
    int round = 0;
    PredictedRound predictedRound = null;
    ArrayList<FinishedRound> finishedRounds = new ArrayList<>();

    ReadOnlyGameScoreManager(long[] selectedPlayers) {
        this.playerCount = selectedPlayers.length;
        this.selectedPlayers = selectedPlayers;

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

    @Nullable
    public Map<Long, Integer> getPredictions(int round) {
        if (round >= 0 && round < this.round) {
            return finishedRounds.get(round).getPredictions();
        } else if (round == this.round && predictedRound != null) {
            return predictedRound.getPredictions();
        } else {
            return null;
        }
    }

    @Nullable
    public Map<Long, Integer> getScores(int round) {
        if (round >= 0 && round < this.round) {
            return finishedRounds.get(round).getScores();
        } else {
            return null;
        }
    }

    /**
     * get Map of results
     * @return Map, key = playerID, value = result
     */
    public Map<Long, Integer> getResults() {
        Map<Long, Integer> results = new HashMap<>();

        for (long playerID: selectedPlayers) {
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

    public GameScoreManager.EntryType getLastEntryType() {
        return getNextEntryType() == EntryType.PREDICTION ? EntryType.SCORE : EntryType.PREDICTION;
    }

    public GameScoreManager.EntryType getNextEntryType() {
        return predictedRound == null ? GameScoreManager.EntryType.PREDICTION : GameScoreManager.EntryType.SCORE;
    }
}
