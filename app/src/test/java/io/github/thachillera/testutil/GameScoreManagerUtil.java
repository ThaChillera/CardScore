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
    public static GameScoreManager getPseudoRandomGame(int playerCount, int roundsPlayed) {
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

    /**
     * Simulate a simple game
     * Player 1 always predicts and gets every round
     * Player 2 always predicts and gets 0
     *
     * @param roundsPlayed
     * @return
     */
    public static GameScoreManager getSimpleGame(int roundsPlayed) {
        int playerCount = 2;

        long[] players = new long[playerCount];
        for (int i = 0; i < playerCount; i++) {
            players[i] = i + 1;
        }

        GameScoreManager gameScoreManager = new GameScoreManager(players);

        for (int i = 0; i < roundsPlayed + 1; i++) {
            Map<Long, Integer> predictions = new HashMap();
            predictions.put(1L, i + 1);
            predictions.put(2L, 0);

            gameScoreManager.enterPredictions(predictions);

            Map<Long, Integer> scores = new HashMap();
            scores.put(1L, i + 1);
            scores.put(2L, 0);

            gameScoreManager.enterScores(scores);
        }

        int[] expectedResult = getSimpleGameExpectedScores(2, roundsPlayed);

        if (gameScoreManager.getResult(1) != expectedResult[0]
                && gameScoreManager.getResult(2) != expectedResult[1]) {
            throw new IllegalStateException("scores are incorrect");
        }

        return gameScoreManager;
    }

    /**
     * Get Expected scores for a 'simple' game, where p1 predicts & wins every round
     * @param playerCount
     * @param roundsPlayed
     * @return
     */
    public static int[] getSimpleGameExpectedScores(int playerCount, int roundsPlayed) {
        if (roundsPlayed < 0) {
            return new int[playerCount];
        } else {
            int[] score = new int[playerCount];
            for (int i = 0; i < playerCount; i++) {
                if (i == 0) {
                    score[i] = (roundsPlayed + 1) * 10 + facorialScore(roundsPlayed);
                } else {
                    score[i] = (roundsPlayed + 1) * 10;
                }
            }
            return score;
        }
    }

    private static int facorialScore(int round) {
        if (round == 0) {
            return 2;
        } else {
            return 2 * (round + 1) + facorialScore(round - 1);
        }
    }
}
