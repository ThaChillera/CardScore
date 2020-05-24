package io.github.thachillera.cardsscorekeeper.data.game.boerenBridge;

import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import io.github.thachillera.testutil.IntMatrixConverter;
import io.github.thachillera.testutil.IntArrayConverter;
import io.github.thachillera.testutil.LongArrayConverter;

class GameScoreManagerTest {
    private static final String FAKE_SELECTED_PLAYERS_THREE = "'1,2,3'", FAKE_SELECTED_PLAYERS_FIVE = "'1,2,3,4,5'", FAKE_SELECTED_PLAYERS_EIGHT = "'1,2,3,4,5,6,7,8'";

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.Test
    void getRound() {
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
    void getPredictions() {
    }

    @org.junit.jupiter.api.Test
    void getScores() {
    }

    @org.junit.jupiter.api.Test
    void getResults() {
    }

    @org.junit.jupiter.api.Test
    void getResult() {
    }

    @org.junit.jupiter.api.Test
    void getLastEntryType() {
    }

    @org.junit.jupiter.api.Test
    void getNextEntryType() {
    }

    @ParameterizedTest
    @CsvSource({
            FAKE_SELECTED_PLAYERS_THREE +
                    ", '[0,1,1],[0,1,1],[0,2,2],[0,2,2],[0,3,3],[0,3,3],[0,4,4],[0,4,4],[0,5,5],[0,5,5],[0,6,6],[0,6,6],[0,7,7],[0,7,7],[0,8,8],[0,8,8],[0,9,9]" +
                    ",[0,8,8],[0,8,8],[0,7,7],[0,7,7],[0,6,6],[0,6,6],[0,5,5],[0,5,5],[0,4,4],[0,4,4],[0,3,3],[0,3,3],[0,2,2],[0,2,2],[0,1,1],[0,1,1]'," +

                    " '[0,1,0],[0,1,1],[0,2,1],[0,2,2],[0,3,2],[0,3,3],[0,4,3],[0,4,4],[0,5,4],[0,5,5],[0,6,5],[0,6,6],[0,7,6],[0,7,7],[0,8,7],[0,8,8],[0,9,8]" +
                    ",[0,8,8],[0,8,7],[0,7,7],[0,7,6],[0,6,6],[0,6,5],[0,5,5],[0,5,4],[0,4,4],[0,4,3],[0,3,3],[0,3,2],[0,2,2],[0,2,1],[0,1,1],[0,1,0]'," +

                    " '[10,12,-2],[20,24,10],[30,38,8],[40,52,22],[50,68,20],[60,84,36],[70,102,34],[80,120,52],[90,140,50],[100,160,70],[110,182,68],[120,204,90],[130,228,88],[140,252,112],[150,278,110],[160,304,136],[170,332,134]" +
                    ",[180,358,160],[190,384,158],[200,408,182],[210,432,180],[220,454,202],[230,476,200],[240,496,220],[250,516,218],[260,534,236],[270,552,234],[280,568,250],[290,584,248],[300,598,262],[310,612,260],[320,624,272],[330,636,270]'",
            FAKE_SELECTED_PLAYERS_FIVE + ", '[1,2,3]', '0,5,9,10,11,18'" ,
            FAKE_SELECTED_PLAYERS_EIGHT + ", '1,4,6,5,3,1', '0,3,5,6,8,10'"
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