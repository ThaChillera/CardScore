package io.github.thachillera.cardsscorekeeper.data.game.boerenBridge;

import org.junit.Assert;
import org.junit.function.ThrowingRunnable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import io.github.thachillera.testutil.GameScoreManagerUtil;
import io.github.thachillera.testutil.converters.IntMatrixConverter;
import io.github.thachillera.testutil.converters.IntArrayConverter;
import io.github.thachillera.testutil.converters.LongArrayConverter;

class GameScoreManagerTest {
    private static final String FAKE_SELECTED_PLAYERS_THREE = "'1,2,3'", FAKE_SELECTED_PLAYERS_FIVE = "'1,2,3,4,5'", FAKE_SELECTED_PLAYERS_EIGHT = "'1,2,3,4,5,6,7,8'";

    @Test
    void getRound() {
        GameScoreManager gameScoreManager = GameScoreManagerUtil.getPseudoRandomGame(3,11);

        Assert.assertEquals(12, gameScoreManager.getRound());
    }

    @ParameterizedTest
    @CsvSource({
            "17, " + FAKE_SELECTED_PLAYERS_THREE,
            "10, " + FAKE_SELECTED_PLAYERS_FIVE,
            "6, " + FAKE_SELECTED_PLAYERS_EIGHT
    })
    void getMaxCards(int expected, @ConvertWith(LongArrayConverter.class) long[] fakePlayers) {
        Assert.assertEquals(expected, new GameScoreManager(fakePlayers).getMaxCards());
    }

    @ParameterizedTest
    @CsvSource({
            "33, " + FAKE_SELECTED_PLAYERS_THREE,
            "19, " + FAKE_SELECTED_PLAYERS_FIVE,
            "11, " + FAKE_SELECTED_PLAYERS_EIGHT
    })
    void getAmountOfRounds(int expected, @ConvertWith(LongArrayConverter.class) long[] fakePlayers) {
        Assert.assertEquals(expected, new GameScoreManager(fakePlayers).getAmountOfRounds());
    }

    @ParameterizedTest
    @CsvSource({
            FAKE_SELECTED_PLAYERS_THREE + ", '1,6,17,16,11,1', '0,5,16,17,22,32'",
            FAKE_SELECTED_PLAYERS_FIVE + ", '1,6,10,9,8,1', '0,5,9,10,11,18'" ,
            FAKE_SELECTED_PLAYERS_EIGHT + ", '1,4,6,5,3,1', '0,3,5,6,8,10'"
    })
    void getCardCount(@ConvertWith(LongArrayConverter.class) long[] fakePlayers,
                      @ConvertWith(IntArrayConverter.class) int[] expectedCardCount,
                      @ConvertWith(IntArrayConverter.class) int[] roundNumbers) {
        GameScoreManager manager = new GameScoreManager(fakePlayers);

        for (int i = 0; i < roundNumbers.length; i++) {
            Assert.assertEquals(expectedCardCount[i], manager.getCardCount(roundNumbers[i]));
        }
    }

    @org.junit.jupiter.api.Test
    void setAndGetPredictions() {
        GameScoreManager gameScoreManager = new GameScoreManager(new long[]{1L, 2L, 3L});

        Map values = new HashMap<Long, Integer>();
        values.put(1L, 0); values.put(2L, 0); values.put(3L, 1);
        gameScoreManager.enterPredictions(values);

        Assert.assertNull(gameScoreManager.getPredictions(3));

        final Map predictions = gameScoreManager.getPredictions(0);
        Assert.assertEquals(predictions, values);

        Assert.assertThrows(UnsupportedOperationException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                predictions.put(4L, 0);
            }
        });
    }

    @Test
    void setAndGetScores() {
        GameScoreManager gameScoreManager = new GameScoreManager(new long[]{1L, 2L, 3L});

        Map values = new HashMap<Long, Integer>();
        values.put(1L, 0); values.put(2L, 0); values.put(3L, 1);
        gameScoreManager.enterPredictions(values);

        values = new HashMap<Long, Integer>();
        values.put(1L, 1); values.put(2L, 0); values.put(3L, 0);
        gameScoreManager.enterScores(values);

        Assert.assertNull(gameScoreManager.getScores(3));

        final Map scores = gameScoreManager.getScores(0);
        Assert.assertEquals(scores, values);

        Assert.assertThrows(UnsupportedOperationException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                scores.put(4L, 0);
            }
        });
    }

    @Test
    void enterValues() {
        GameScoreManager gameScoreManager = new GameScoreManager(new long[]{1L, 2L, 3L});

        Map predictions = new HashMap<Long, Integer>();
        predictions.put(1L, 0); predictions.put(2L, 0); predictions.put(3L, 1);
        gameScoreManager.enterValues(predictions);

        Map scores = new HashMap<Long, Integer>();
        scores.put(1L, 1); scores.put(2L, 0); scores.put(3L, 0);
        gameScoreManager.enterValues(scores);

        final Map gotPredictions = gameScoreManager.getPredictions(0);
        final Map gotScores = gameScoreManager.getScores(0);
        Assert.assertEquals(predictions, gotPredictions);
        Assert.assertEquals(scores, gotScores);
    }

    @Test
    void getResults() {
        GameScoreManager gameScoreManager = new GameScoreManager(new long[]{1L, 2L, 3L});

        Map predictions = new HashMap<Long, Integer>();
        predictions.put(1L, 0); predictions.put(2L, 0); predictions.put(3L, 1);
        gameScoreManager.enterValues(predictions);

        Map scores = new HashMap<Long, Integer>();
        scores.put(1L, 1); scores.put(2L, 0); scores.put(3L, 0);
        gameScoreManager.enterValues(scores);

        Map expectedResults = new HashMap<Long, Integer>();
        expectedResults.put(1L, -2); expectedResults.put(2L, 10); expectedResults.put(3L, -2);

        final Map results = gameScoreManager.getResults();
        Assert.assertEquals(expectedResults, results);

        Assert.assertThrows(UnsupportedOperationException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                results.put(4L, 0);
            }
        });
    }

    @Test
    void getResult() {
        GameScoreManager gameScoreManager = new GameScoreManager(new long[]{1L, 2L, 3L});

        Map predictions = new HashMap<Long, Integer>();
        predictions.put(1L, 0); predictions.put(2L, 0); predictions.put(3L, 1);
        gameScoreManager.enterValues(predictions);

        Map scores = new HashMap<Long, Integer>();
        scores.put(1L, 1); scores.put(2L, 0); scores.put(3L, 0);
        gameScoreManager.enterValues(scores);

        Assert.assertEquals(-2, gameScoreManager.getResult(1));
        Assert.assertEquals(10, gameScoreManager.getResult(2));
        Assert.assertEquals(-2, gameScoreManager.getResult(3));
    }

    @Test
    void undo() {
        GameScoreManager gameScoreManager = GameScoreManagerUtil.getSimpleGame(11);

        for (int i = 11; i >= 0; i--) {
            //undo enter score
            gameScoreManager.undo();

            Assert.assertNull(gameScoreManager.getScores(i));

            //undo enter prediction
            gameScoreManager.undo();
            Assert.assertNull(gameScoreManager.getPredictions(i));

            int[] expectedResults = GameScoreManagerUtil.getSimpleGameExpectedScores(2, i-1);
            Map results = gameScoreManager.getResults();
            Assert.assertEquals(expectedResults[0], results.get(1L));
            Assert.assertEquals(expectedResults[1], results.get(2L));
        }
    }

    @Test
    void getSaveGameData() {
        GameScoreManager gameScoreManager = new GameScoreManager(new long[]{1L, 2L, 3L});

        Map values = new HashMap<Long, Integer>();
        values.put(1l, 0); values.put(2l, 0); values.put(3l, 1);
        gameScoreManager.enterPredictions(values);

        String save = gameScoreManager.getSaveGameData();

        Assert.assertNotNull(save);
        Assert.assertFalse(save.isEmpty());
    }

    @Test
    void loadGameData() {
        GameScoreManager saveGame = GameScoreManagerUtil.getPseudoRandomGame(3, 11);

        GameScoreManager loadedGame = GameScoreManager.loadGameData(saveGame.getSaveGameData());

        Assert.assertEquals(saveGame.getAmountOfRounds(), loadedGame.getAmountOfRounds());
        Assert.assertEquals(saveGame.getMaxCards(), loadedGame.getMaxCards());
        Assert.assertEquals(saveGame.getLastEntryType(), loadedGame.getLastEntryType());
        Assert.assertEquals(saveGame.getNextEntryType(), loadedGame.getNextEntryType());
        Assert.assertEquals(saveGame.getRound(), loadedGame.getRound());

        for (int i = 0; i < 11; i++) {
            Assert.assertEquals(saveGame.getPredictions(i), loadedGame.getPredictions(i));
            Assert.assertEquals(saveGame.getScores(i), loadedGame.getScores(i));
            Assert.assertEquals(saveGame.getResults(), loadedGame.getResults());
        }
    }

    @Test
    void getLastEntryType() {
        GameScoreManager gameScoreManager = new GameScoreManager(new long[]{1l,2l,3l});
        Assert.assertSame(ReadOnlyGameScoreManager.EntryType.SCORE, gameScoreManager.getLastEntryType());

        Map values = new HashMap<Long, Integer>();
        values.put(1l, 0); values.put(2l, 0); values.put(3l, 1);
        gameScoreManager.enterPredictions(values);
        Assert.assertSame(ReadOnlyGameScoreManager.EntryType.PREDICTION, gameScoreManager.getLastEntryType());

        gameScoreManager.enterScores(values);
        Assert.assertSame(ReadOnlyGameScoreManager.EntryType.SCORE, gameScoreManager.getLastEntryType());
    }

    @Test
    void getNextEntryType() {
        GameScoreManager gameScoreManager = new GameScoreManager(new long[]{1L, 2L, 3L});
        Assert.assertSame(ReadOnlyGameScoreManager.EntryType.PREDICTION, gameScoreManager.getNextEntryType());

        Map values = new HashMap<Long, Integer>();
        values.put(1L, 0); values.put(2L, 0); values.put(3L, 1);
        gameScoreManager.enterPredictions(values);
        Assert.assertSame(ReadOnlyGameScoreManager.EntryType.SCORE, gameScoreManager.getNextEntryType());

        gameScoreManager.enterScores(values);
        Assert.assertSame(ReadOnlyGameScoreManager.EntryType.PREDICTION, gameScoreManager.getNextEntryType());
    }

    @Test
    void getSelectedPlayers() {
        long[] sourcePlayers = new long[] {1L, 2L, 3L};

        GameScoreManager gameScoreManager = new GameScoreManager(sourcePlayers);

        long[] copyOfPlayers = gameScoreManager.getSelectedPlayers();
        Assert.assertArrayEquals(sourcePlayers, copyOfPlayers);

        copyOfPlayers[0] = 100L;
        Assert.assertArrayEquals(sourcePlayers, gameScoreManager.getSelectedPlayers());
    }

    /**
     * Play a full game with 3, 5 and 8 players.
     *
     * @param fakePlayers
     * @param predictedRoundsWon
     * @param actualRoundsWon
     * @param resultingScore
     */
    @ParameterizedTest
    @CsvSource({
            FAKE_SELECTED_PLAYERS_THREE +
                    ", '[0,1,1],[0,1,1],[0,2,2],[0,2,2],[0,3,3],[0,3,3],[0,4,4],[0,4,4],[0,5,5],[0,5,5],[0,6,6],[0,6,6],[0,7,7],[0,7,7],[0,8,8],[0,8,8],[0,9,9]" +
                    ",[0,8,8],[0,8,8],[0,7,7],[0,7,7],[0,6,6],[0,6,6],[0,5,5],[0,5,5],[0,4,4],[0,4,4],[0,3,3],[0,3,3],[0,2,2],[0,2,2],[0,1,1],[0,1,1]'," +

                    " '[0,1,0],[0,1,1],[0,2,1],[0,2,2],[0,3,2],[0,3,3],[0,4,3],[0,4,4],[0,5,4],[0,5,5],[0,6,5],[0,6,6],[0,7,6],[0,7,7],[0,8,7],[0,8,8],[0,9,8]" +
                    ",[0,8,8],[0,8,7],[0,7,7],[0,7,6],[0,6,6],[0,6,5],[0,5,5],[0,5,4],[0,4,4],[0,4,3],[0,3,3],[0,3,2],[0,2,2],[0,2,1],[0,1,1],[0,1,0]'," +

                    " '[10,12,-2],[20,24,10],[30,38,8],[40,52,22],[50,68,20],[60,84,36],[70,102,34],[80,120,52],[90,140,50],[100,160,70],[110,182,68],[120,204,90],[130,228,88],[140,252,112],[150,278,110],[160,304,136],[170,332,134]" +
                    ",[180,358,160],[190,384,158],[200,408,182],[210,432,180],[220,454,202],[230,476,200],[240,496,220],[250,516,218],[260,534,236],[270,552,234],[280,568,250],[290,584,248],[300,598,262],[310,612,260],[320,624,272],[330,636,270]'",

            FAKE_SELECTED_PLAYERS_FIVE +
                    ", '[0,0,0,0,0],[0,1,0,1,0],[0,1,1,0,1],[0,1,1,1,1],[0,2,1,1,1],[0,1,2,1,2],[0,1,3,2,1],[0,3,2,1,2],[0,2,3,2,3]" +
                    ",[0,2,2,3,4],[0,2,2,3,2],[0,1,2,1,3],[0,2,1,1,3],[0,0,1,2,3],[0,1,1,1,1],[0,1,0,1,1],[0,1,1,0,1],[0,0,0,0,2],[0,0,0,0,0]'," +

                    " '[0,0,0,0,1],[0,1,0,0,1],[0,1,0,0,2],[0,1,1,1,1],[0,2,1,1,1],[0,1,2,1,2],[0,1,3,2,1],[0,3,2,1,2],[0,2,3,2,2],[0,2,2,3,3]" +
                    ",[0,2,2,3,2],[0,1,2,2,3],[0,2,1,1,3],[0,0,1,2,3],[0,1,1,2,1],[0,1,0,1,2],[0,1,1,0,1],[0,0,0,0,2],[0,0,0,0,1]'," +

                    " '[10,10,10,10,-2],[20,22,20,8,-4],[30,34,18,18,-6],[40,46,30,30,6],[50,60,42,42,18],[60,72,56,54,32],[70,84,72,68,44],[80,100,86,80,58],[90,114,102,94,56],[100,128,116,110,54],[110,142,130,126,68]" +
                    ",[120,154,144,124,84],[130,168,156,136,100],[140,178,168,150,116],[150,190,180,148,128],[160,202,190,160,126],[170,214,202,170,138],[180,224,212,180,152],[190,234,222,190,150]'" ,

            FAKE_SELECTED_PLAYERS_EIGHT +
                    ", '[0,0,0,0,0,0,0,0],[0,1,0,1,0,0,0,0],[0,1,1,0,1,0,1,1],[0,1,1,1,1,0,0,1],[0,2,1,1,1,1,0,0]" +
                    ",[0,1,2,1,2,1,1,1],[0,1,2,1,1,0,1,0],[0,2,1,1,0,1,0,1],[0,0,1,1,1,0,1,0],[0,0,1,0,1,0,1,0],[0,0,1,0,0,0,1,0]'," +

                    " '[0,0,0,0,0,1,0,0],[0,1,0,1,0,0,0,0],[0,1,1,0,0,0,0,1],[0,1,1,1,0,0,0,1],[0,2,1,1,1,0,0,0]" +
                    ",[0,1,2,1,0,1,0,1],[0,1,2,0,1,0,1,0],[0,2,1,1,0,0,0,0],[0,0,1,0,1,0,1,0],[0,0,1,0,0,0,1,0],[0,0,1,0,0,0,0,0]'," +

                    " '[10,10,10,10,10,-2,10,10],[20,22,20,22,20,8,20,20],[30,34,32,32,18,18,18,32],[40,46,44,44,16,28,28,44],[50,60,56,56,28,26,38,54],[60,72,70,68,24,38,36,66]" +
                    ",[70,84,84,66,36,48,48,76],[80,98,96,78,46,46,58,74],[90,108,108,76,58,56,70,84],[100,118,120,86,56,66,82,94],[110,128,132,96,66,76,80,104]'"
    })
    void testFullPlaythrough(@ConvertWith(LongArrayConverter.class) long[] fakePlayers,
                      @ConvertWith(IntMatrixConverter.class) int[][] predictedRoundsWon,
                      @ConvertWith(IntMatrixConverter.class) int[][] actualRoundsWon,
                      @ConvertWith(IntMatrixConverter.class) int[][] resultingScore) {
        GameScoreManager manager = new GameScoreManager(fakePlayers);

        for (int i = 0; i < manager.getAmountOfRounds(); i++) {
            Map<Long, Integer> predictions = new HashMap<>();
            for (int j = 0; j < fakePlayers.length; j++) {
                predictions.put(fakePlayers[j],predictedRoundsWon[i][j]);
            }
            manager.enterPredictions(predictions);

            Map<Long, Integer> scores = new HashMap<>();
            for (int j = 0; j < fakePlayers.length; j++) {
                scores.put(fakePlayers[j],actualRoundsWon[i][j]);
            }
            manager.enterScores(scores);

            for (int j = 0; j < fakePlayers.length; j++) {
                Assert.assertEquals(resultingScore[i][j], manager.getResult(fakePlayers[j]));
            }
        }
    }
}