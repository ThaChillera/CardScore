package io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.rounds;


import io.github.thachillera.cardsscorekeeper.data.players.PlayerManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class RoundData {
    final private Map<Long, Integer> predictions, scores, results;
    final private int playerCount, cardCount;

    RoundData(int playerCount, int cardCount, Map<Long, Integer> predictions) {
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

        for (long playerID: predictions.keySet()) {
            this.predictions.put(playerID, predictions.get(playerID));
        }
    }

    FinishedRound addScores(Map<Long, Integer> scores) {
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

        for (long playerID: scores.keySet()) {
            this.scores.put(playerID, scores.get(playerID));
        }

        calculateScores(scores.keySet().toArray(new Long[scores.size()]));
        return new FinishedRound(this);
    }

    private void calculateScores(Long[] players) {
        //calculate results
        for (long playerID: players) {
            results.put(playerID,
                    predictions.get(playerID).equals(scores.get(playerID))
                        ? 10 + (2* scores.get(playerID))
                        : -(2 * Math.abs(scores.get(playerID) - predictions.get(playerID) )));
        }
    }

    Map<Long, Integer> getPredictions() {
        return Collections.unmodifiableMap(this.predictions);
    }

    Map<Long, Integer> getScores() {
        return Collections.unmodifiableMap(this.scores);
    }

    Map<Long, Integer> getResults() {
        return Collections.unmodifiableMap(this.results);
    }
}
