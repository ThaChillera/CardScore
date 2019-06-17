package com.robinkuiper.cardsscorekeeper.data.game.boerenBridge;

import com.robinkuiper.cardsscorekeeper.interfaces.boerenBridge.PlayerHeader;

import java.util.ArrayList;

public class GameScoreManager {
    private RoundScoreManager[] predictions, scores;
    private ArrayList<PlayerHeader> playerHeaders;

    GameScoreManager(int rounds, ArrayList<PlayerHeader> playerHeaders) {
        this.playerHeaders = playerHeaders;
        predictions = new RoundScoreManager[rounds];
        scores = new RoundScoreManager[rounds];

        for (int i = 0; i < rounds; i++) {
            predictions[i] = new RoundScoreManager(this, RoundScoreManagerType.PREDICTIONS);
            scores[i] = new RoundScoreManager(this, RoundScoreManagerType.SCORES);
        }
    }

    RoundScoreManager getPredictionRoundScoreManager(int i) {
        return predictions[i];
    }

    RoundScoreManager getEnterRoundScoreManager(int i) {
        return scores[i];
    }

    void enterScores(int[] scores, RoundScoreManagerType type) {
        for (int i = 0; i < scores.length; i++) {
            if (type == RoundScoreManagerType.PREDICTIONS) {
                playerManager.addPlayerPrediction(selectedPlayers[i], scores[i]);
            } else {
                playerManager.addPlayerScore(selectedPlayers[i], scores[i]);
            }

            playerHeaders.get(i).setPlayerScoreView(playerManager.getPlayerScore(selectedPlayers[i]));
        }
    }
}
