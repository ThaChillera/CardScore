package com.robinkuiper.cardsscorekeeper.data;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayerManager {
    private static final String PLAYERDATALOCATION = "playerdata.json";
    private static final PlayerManager ourInstance = new PlayerManager();
    private ArrayList<Player> players;
    private boolean[] selectedPlayers;

    public static PlayerManager getInstance() {
        return ourInstance;
    }

    private PlayerManager() {
    }

    public int getPlayerCount() {
        return players.size();
    }

    public int getSelectedPlayerCount() {
        int selectedCount = 0;
        for (int i = 0; i < selectedPlayers.length; i++) {
            if (selectedPlayers[i]) {
                ++selectedCount;
            }
        }

        return selectedCount;
    }

    public int[] getSelectedPlayerIds() {
        int[] returnValues = new int[getSelectedPlayerCount()];
        int position = 0;

        for (int i = 0; i < selectedPlayers.length; i++) {
            if (selectedPlayers[i]) {
                returnValues[position] = i;
                ++position;
            }
        }

        return returnValues;
    }

    public String getPlayerName(int playerId) {
        return players.get(playerId).getName();
    }

    public String getPlayerShortName(int playerId) {
        return players.get(playerId).getShortName();
    }

    public int getPlayerScore(int playerId) {
        return players.get(playerId).getScore();
    }

    public void addPlayerPrediction(int playerId, int prediction) {
        players.get(playerId).addPrediction(prediction);
    }

    public void addPlayerScore(int playerId, int score) {
        players.get(playerId).addScore(score);
    }

    public void addPlayer(String name, String shortName) {
        players.add(new Player(name, shortName));
    }

    public void editPlayer(int playerId, String name, String shortName) {
        players.get(playerId).editPlayer(name, shortName);
    }

    public void deletePlayer(int playerId) {
        players.remove(playerId);
    }

    public void selectPlayer(int playerId, boolean selected) {
        selectedPlayers[playerId] = selected;
    }

    public void loadPlayerData(Context context) {
        try {
            FileInputStream inputStream = context.openFileInput(PLAYERDATALOCATION);
            players = new ArrayList<>(Arrays.asList(new Gson().fromJson(new InputStreamReader(inputStream), Player[].class)));

        } catch (IOException exception) {
            players = new ArrayList<>();
        }

        selectedPlayers = new boolean[getPlayerCount()];
    }

    public void savePlayerData(Context context) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            for (Player player: players) {
                player.reset();
            }

            FileOutputStream outputStream =context.openFileOutput(PLAYERDATALOCATION, Context.MODE_PRIVATE);
            outputStream.write(gson.toJson(players.toArray()).getBytes());
            outputStream.close();

        } catch (IOException exception) {
            Toast.makeText(context, "PlayerManager failed to save, some info could be lost", Toast.LENGTH_LONG).show();
        }
    }
}
