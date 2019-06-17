package com.robinkuiper.cardsscorekeeper.data.players;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robinkuiper.cardsscorekeeper.BuildConfig;

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
    private ArrayList<Integer> selectedPlayers = new ArrayList<>();

    public static PlayerManager getInstance() {
        return ourInstance;
    }

    private PlayerManager() {
    }

    public int getAllPlayersCount() {
        return players.size();
    }

    public int getSelectedPlayerCount() {
        return selectedPlayers.size();
    }

    public int[] getSelectedPlayers() {
        int[] returnValues = new int[getSelectedPlayerCount()];

        for (int i = 0; i < selectedPlayers.size(); i++) {
            returnValues[i] = selectedPlayers.get(i);
        }

        return returnValues;
    }

    public String getPlayerName(int playerId) {
        return players.get(playerId).getName();
    }

    public String getPlayerShortName(int playerId) {
        return players.get(playerId).getShortName();
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

    public void selectPlayer(int playerId) {
        if (!isPlayerSelected(playerId)) {
            selectedPlayers.add(playerId);
        }
    }

    public void deselectPlayer(int playerId) {
        if (isPlayerSelected(playerId)) {
            selectedPlayers.remove((Integer) playerId);
        }
    }

    public boolean isPlayerSelected(int playerId) {
        return selectedPlayers.contains(playerId);
    }

    public void loadPlayerData(Context context) {
        try {
            FileInputStream inputStream = context.openFileInput(PLAYERDATALOCATION);
            players = new ArrayList<>(Arrays.asList(new Gson().fromJson(new InputStreamReader(inputStream), Player[].class)));

        } catch (IOException exception) {
            Toast.makeText(context, "PlayerManager failed to load, some info could be lost", Toast.LENGTH_LONG).show();
            players = new ArrayList<>();
        }

        //Junk Players
        if (BuildConfig.DEBUG && players.isEmpty()) {
            addPlayer("Test Player 1", "Test1");
            addPlayer("Test PLayer 2", "Test2");
        }
    }

    public void savePlayerData(Context context) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            FileOutputStream outputStream =context.openFileOutput(PLAYERDATALOCATION, Context.MODE_PRIVATE);
            outputStream.write(gson.toJson(players.toArray()).getBytes());
            outputStream.close();

        } catch (IOException exception) {
            Toast.makeText(context, "PlayerManager failed to save, some info could be lost", Toast.LENGTH_LONG).show();
        }
    }
}
