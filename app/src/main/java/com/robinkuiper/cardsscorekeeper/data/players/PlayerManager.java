package com.robinkuiper.cardsscorekeeper.data.players;

import android.content.Context;
import android.util.Pair;
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
import java.util.UUID;

public class PlayerManager {
    private static final String PLAYERDATALOCATION = "playerdata.json";
    private static final PlayerManager ourInstance = new PlayerManager();
    private ArrayList<Player> players;
    private ArrayList<Long> selectedPlayers = new ArrayList<>();

    public static PlayerManager getInstance() {
        return ourInstance;
    }

    private PlayerManager() {
    }

    private Player getPlayer(long playerId) {
        for(Player player: players) {
            if (player.getId() == playerId) {
                return player;
            }
        }
        return null;
    }

    public int getAllPlayersCount() {
        return players.size();
    }

    public ArrayList<Pair<Long, String>> getAllPlayers() {
        ArrayList<Pair<Long, String>> allPlayers = new ArrayList<>();
        for(Player player: players) {
            allPlayers.add(new Pair<>(player.getId(), player.getName()));
        }
        return allPlayers;
    }

    public void setSelectedPlayers(ArrayList<Pair<Long, String>> selectedPlayers) {
        this.selectedPlayers.clear();
        for(Pair<Long, String> selectedPlayer: selectedPlayers) {
            this.selectedPlayers.add(selectedPlayer.first);
        }
    }

    public int getSelectedPlayerCount() {
        return selectedPlayers.size();
    }

    public long[] getSelectedPlayers() {
        long[] returnValues = new long[getSelectedPlayerCount()];

        for (int i = 0; i < selectedPlayers.size(); i++) {
            returnValues[i] = selectedPlayers.get(i);
        }

        return returnValues;
    }

    public String getPlayerName(long playerId) {
        return getPlayer(playerId).getName();
    }

    public String getPlayerShortName(long playerId) {
        return getPlayer(playerId).getShortName();
    }

    public void addPlayer(String name, String shortName) {
        //get unique ID
        long uniqueId = System.currentTimeMillis();
        while (getPlayer(uniqueId) != null) {
            uniqueId = System.currentTimeMillis();
        }

        players.add(new Player(uniqueId, name, shortName));
    }

    public void editPlayer(long playerId, String name, String shortName) {
        getPlayer(playerId).editPlayer(name, shortName);
    }

    public void deletePlayer(long playerId) {
        players.remove(getPlayer(playerId));
    }

    public void selectPlayer(long playerId) {
        if (!isPlayerSelected(playerId)) {
            selectedPlayers.add(playerId);
        }
    }

    public void deselectPlayer(long playerId) {
        if (isPlayerSelected(playerId)) {
            selectedPlayers.remove(playerId);
        }
    }

    public boolean isPlayerSelected(long playerId) {
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
            for (int i = 0; i < 10; i++) {
                addPlayer("Test PLayer " + i, "T" + i);
            }
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
