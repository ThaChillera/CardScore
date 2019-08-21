package io.github.thachillera.cardsscorekeeper.data.game.boerenBridge;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.rounds.FinishedRound;
import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.rounds.PredictedRound;
import io.github.thachillera.cardsscorekeeper.data.players.PlayerManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReadOnlyGameScoreManager {

    private transient final static String GAMEDATAPREFIX = "gamedata_", GAMESCOREMANAGERLOCATION = GAMEDATAPREFIX + "manager.json", SELECTEDPLAYERSLOCATION = GAMEDATAPREFIX + "selectedplayers.json";

    transient PlayerManager playerManager = PlayerManager.getInstance();
    transient Context context;

    //current round
    int round = 0;
    final int playerCount;

    private final int maxCards, amountOfRounds;

    PredictedRound predictedRound = null;
    ArrayList<FinishedRound> finishedRounds = new ArrayList<>();

    ReadOnlyGameScoreManager(final Context context, int playerCount) {
        this.context = context;
        this.playerCount = playerCount;

        maxCards = 52 % playerCount == 0 ? (52 - playerCount) / playerCount : 52 / playerCount;
        amountOfRounds = ((maxCards * 2)) - 1;
    }

    public int getRound() {
        return round;
    }

    public int getMaxCards() {
        return maxCards;
    }

    public int getAmountOfRounds() {
        return amountOfRounds;
    }

    /**
     * get card count for given round
     * @param round zero-based round number
     * @return card count
     */
    public int getCardCount(int round) {
        round += 1;
        return round <= maxCards ? round : maxCards - (round - maxCards);
    }

    @Nullable
    public Map<Long, Integer> getPredictions(int round) {
        if (round >= 0 && round < this.round) {
            return finishedRounds.get(round).getPredictions();
        } else if (round == this.round && predictedRound != null) {
            return predictedRound.getPredictions();
        } else {
            return null;
        }
    }

    @Nullable
    public Map<Long, Integer> getScores(int round) {
        if (round >= 0 && round < this.round) {
            return finishedRounds.get(round).getScores();
        } else {
            return null;
        }
    }

    /**
     * get Map of results
     * @return Map, key = playerID, value = result
     */
    public Map<Long, Integer> getResults() {
        Map<Long, Integer> results = new HashMap<>();

        for (long playerID: playerManager.getSelectedPlayers()) {
            results.put(playerID, getResult(playerID));
        }

        return Collections.unmodifiableMap(results);
    }

    /**
     * get result of given player ID
     * @param playerID ID of player
     * @return result
     */
    public int getResult(long playerID) {
        int result = 0;
        for (FinishedRound round: finishedRounds) {
            result += round.getResults().get(playerID);
        }

        return result;
    }

    public enum EntryType {
        PREDICTION, SCORE
    }

    public GameScoreManager.EntryType getLastEntryType() {
        return getNextEntryType() == EntryType.PREDICTION ? EntryType.SCORE : EntryType.PREDICTION;
    }

    public GameScoreManager.EntryType getNextEntryType() {
        return predictedRound == null ? GameScoreManager.EntryType.PREDICTION : GameScoreManager.EntryType.SCORE;
    }

    public static boolean hasExistingSave(Context context) {
        return new File(context.getFilesDir(),GAMESCOREMANAGERLOCATION).exists() && new File(context.getFilesDir(),SELECTEDPLAYERSLOCATION).exists();
    }

    public static GameScoreManager loadGameData(Context context) {
        PlayerManager playerManager = PlayerManager.getInstance();
        long[] selectedPlayersBackup = playerManager.getSelectedPlayers();
        try {
            FileInputStream inputStream = context.openFileInput(SELECTEDPLAYERSLOCATION);
            playerManager.replaceSelectedPlayers(new Gson().fromJson(new InputStreamReader(inputStream), long[].class));

            inputStream = context.openFileInput(GAMESCOREMANAGERLOCATION);
            GameScoreManager gameScoreManager = new Gson().fromJson(new InputStreamReader(inputStream), GameScoreManager.class);
            gameScoreManager.playerManager = PlayerManager.getInstance();
            gameScoreManager.context = context;
            return gameScoreManager;

        } catch (IOException exception) {
            Toast.makeText(context, "GameManager failed to load, some info could be lost", Toast.LENGTH_LONG).show();
            playerManager.replaceSelectedPlayers(selectedPlayersBackup);
            return new GameScoreManager(context, PlayerManager.getInstance().getSelectedPlayerCount());
        }
    }

    public void saveGameData() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            FileOutputStream outputStream = context.openFileOutput(GAMESCOREMANAGERLOCATION, Context.MODE_PRIVATE);
            outputStream.write(gson.toJson(this).getBytes());
            outputStream.close();

            outputStream = context.openFileOutput(SELECTEDPLAYERSLOCATION, Context.MODE_PRIVATE);
            outputStream.write(gson.toJson(playerManager.getSelectedPlayers()).getBytes());
            outputStream.close();

        } catch (IOException exception) {
            Toast.makeText(context, "GameManager failed to save, some info could be lost", Toast.LENGTH_LONG).show();
        }
    }
}
