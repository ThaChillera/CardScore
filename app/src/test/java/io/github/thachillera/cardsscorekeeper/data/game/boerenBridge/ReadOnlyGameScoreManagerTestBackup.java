//package io.github.thachillera.cardsscorekeeper.data.game.boerenBridge;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import junitparams.JUnitParamsRunner;
//import junitparams.Parameters;
//
//import static org.junit.Assert.*;
//
//@RunWith(JUnitParamsRunner.class)
//public class ReadOnlyGameScoreManagerTestBackup {
//    public static final long[] FAKE_SELECTED_PLAYERS = {1,2,3,4,5};
//    private ReadOnlyGameScoreManager gameScoreManager;
//
//    @Before
//    public void setUp() throws Exception {
//        gameScoreManager = new GameScoreManager(FAKE_SELECTED_PLAYERS);
//    }
//
//    @Test
//    public void getRound() {
//    }
//
//    @Test
//    @Parameters({})
//    public void getMaxCards() {
//        Assert.assertEquals(10, gameScoreManager.getMaxCards());
//        Assert.assertEquals(17, new GameScoreManager(new long[]{1,2,3}).getMaxCards());
//        Assert.assertEquals(6, new GameScoreManager(new long[]{1,2,3,4,5,6,7,8}).getMaxCards());
//    }
//
//    @Test
//    public void getAmountOfRounds() {
//        Assert.assertEquals(19, gameScoreManager.getAmountOfRounds());
//        Assert.assertEquals(33, new GameScoreManager(new long[]{1,2,3}).getAmountOfRounds());
//        Assert.assertEquals(11, new GameScoreManager(new long[]{1,2,3,4,5,6,7,8}).getAmountOfRounds());
//    }
//
//    @Test
//    public void getCardCount() {
//        Assert.assertEquals(1, gameScoreManager.getCardCount(0));
//        Assert.assertEquals(6, gameScoreManager.getCardCount(5));
//        Assert.assertEquals(10, gameScoreManager.getCardCount(9));
//        Assert.assertEquals(9, gameScoreManager.getCardCount(10));
//        Assert.assertEquals(8, gameScoreManager.getCardCount(11));
//        Assert.assertEquals(1, gameScoreManager.getCardCount(19));
//
//        Assert.assertEquals(33, new GameScoreManager(new long[]{1,2,3}).getCardCount());
//        Assert.assertEquals(11, new GameScoreManager(new long[]{1,2,3,4,5,6,7,8}).getAmountOfRounds());
//    }
//
//    @Test
//    public void getPredictions() {
//    }
//
//    @Test
//    public void getScores() {
//    }
//
//    @Test
//    public void getResults() {
//    }
//
//    @Test
//    public void getResult() {
//    }
//
//    @Test
//    public void getLastEntryType() {
//    }
//
//    @Test
//    public void getNextEntryType() {
//    }
//}