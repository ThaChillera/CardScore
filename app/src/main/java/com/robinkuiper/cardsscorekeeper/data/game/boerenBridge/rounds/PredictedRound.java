package com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.rounds;

import java.util.Map;

public class PredictedRound {
    private final RoundData roundData;

    public PredictedRound(int playerCount, int roundCount, Map<Integer, Integer> predictions) {
        this.roundData = new RoundData(playerCount, roundCount, predictions);
    }

    public FinishedRound addScores(Map<Integer, Integer> scores) {
        return roundData.addScores(scores);
    }

    public Map<Integer, Integer> getPredictions() {
        return roundData.getPredictions();
    }
}
