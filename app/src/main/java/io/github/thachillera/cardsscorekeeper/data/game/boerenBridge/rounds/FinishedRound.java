package io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.rounds;

import java.util.Map;

public class FinishedRound {
    private final RoundData roundData;

    FinishedRound(RoundData roundData) {
        this.roundData = roundData;
    }

    public Map<Long, Integer> getPredictions() {
        return roundData.getPredictions();
    }

    public Map<Long, Integer> getScores() {
        return roundData.getScores();
    }

    public Map<Long, Integer> getResults() {
        return roundData.getResults();
    }
}
