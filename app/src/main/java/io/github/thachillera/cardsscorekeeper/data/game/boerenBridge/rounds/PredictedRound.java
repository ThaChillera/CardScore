package io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.rounds;

import java.util.Map;

public class PredictedRound {
    private final RoundData roundData;

    public PredictedRound(int playerCount, int roundCount, Map<Long, Integer> predictions) {
        this.roundData = new RoundData(playerCount, roundCount, predictions);
    }

    public FinishedRound addScores(Map<Long, Integer> scores) {
        return roundData.addScores(scores);
    }

    public Map<Long, Integer> getPredictions() {
        return roundData.getPredictions();
    }
}
