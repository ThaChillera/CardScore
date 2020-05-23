package io.github.thachillera.cardsscorekeeper.data.game.boerenBridge;

import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import io.github.thachillera.testutil.IntArrayConverter;
import io.github.thachillera.testutil.LongArrayConverter;

class ReadOnlyGameScoreManagerTest {
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
}