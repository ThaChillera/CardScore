package com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.rounds;


import com.robinkuiper.cardsscorekeeper.data.players.PlayerManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class RoundData {
    final private Map<Integer, Integer> predictions, scores, results;
    final private int playerCount, cardCount;

    final private PlayerManager playerManager = PlayerManager.getInstance();

    RoundData(int playerCount, int cardCount, Map<Integer, Integer> predictions) {
        this.playerCount = playerCount;
        this.cardCount = cardCount;

        this.predictions = new HashMap<>();
        this.scores = new HashMap<>();
        this.results = new HashMap<>();

        //validate prediction input
        for (int value: predictions.values()) {
            if (value < 0) {
                throw new IllegalArgumentException("Impossible prediction");
            }
        }

        for (int playerID: predictions.keySet()) {
            this.predictions.put(playerID, predictions.get(playerID));
        }
    }

    FinishedRound addScores(Map<Integer, Integer> scores) {
        //validate input
        int totalScore = 0;
        for (int value: scores.values()) {
            if (value < 0) {
                throw new IllegalArgumentException("Impossible score");
            }
            totalScore += value;
        }
        if (totalScore != cardCount)
            throw new IllegalArgumentException("Incorrectly entered score");

        for (int playerID: scores.keySet()) {
            this.scores.put(playerID, scores.get(playerID));
        }

        calculateScores();
        return new FinishedRound(this);
    }

    private void calculateScores() {
        //calculate results
        for (int i = 0; i < playerCount; i++) {
            int playerID = playerManager.getSelectedPlayers()[i];
            results.put(playerID,
                    predictions.get(playerID).equals(scores.get(playerID))
                        ? 10 + (2* scores.get(playerID))
                        : -(2 * Math.abs(scores.get(playerID) - predictions.get(playerID) )));
        }
    }

    Map<Integer, Integer> getPredictions() {
        return Collections.unmodifiableMap(this.predictions);
    }

    Map<Integer, Integer> getScores() {
        return Collections.unmodifiableMap(this.scores);
    }

    Map<Integer, Integer> getResults() {
        return Collections.unmodifiableMap(this.results);
    }
}
