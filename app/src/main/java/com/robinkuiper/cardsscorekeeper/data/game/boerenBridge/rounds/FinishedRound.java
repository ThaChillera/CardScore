package com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.rounds;

import java.util.Map;

public class FinishedRound {
    private final RoundData roundData;

    FinishedRound(RoundData roundData) {
        this.roundData = roundData;
    }

    public Map<Integer, Integer> getPredictions() {
        return roundData.getPredictions();
    }

    public Map<Integer, Integer> getScores() {
        return roundData.getScores();
    }

    public Map<Integer, Integer> getResults() {
        return roundData.getResults();
    }
}
