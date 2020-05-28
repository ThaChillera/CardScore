package io.github.thachillera.testutil;

import java.util.HashMap;
import java.util.Map;

import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.GameScoreManager;

public class GameScoreManagerUtil {
    /**
     * Get a game with a certain amount of played games.
     *
     * Player one always predicts one round, player two always two etc.
     * Player one always wins every round.
     *
     * @param playerCount
     * @param roundsPlayed
     * @return
     */
    public static GameScoreManager getGame(int playerCount, int roundsPlayed) {
        long[] players = new long[playerCount];
        for (int i = 0; i < playerCount; i++) {
            players[i] = i + 1;
        }

        GameScoreManager gameScoreManager = new GameScoreManager(players);

        for (int i = 0; i < roundsPlayed + 1; i++) {
            Map<Long, Integer> predictions = new HashMap();
            for (long playerId : players) {
                predictions.put(playerId, (int) playerId);
            }

            gameScoreManager.enterPredictions(predictions);

            Map<Long, Integer> scores = new HashMap();
            for (long playerId : players) {
                scores.put(playerId, playerId == 1 ? gameScoreManager.getCardCount(i) : 0);
            }

            gameScoreManager.enterScores(scores);
        }

        return gameScoreManager;
    }
}
