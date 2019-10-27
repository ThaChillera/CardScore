package io.github.thachillera.cardsscorekeeper.data.players;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.thachillera.cardsscorekeeper.BuildConfig;

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
    private ArrayList<Long> selectedPlayers = new ArrayList<>();

    public static PlayerManager getInstance() {
        return ourInstance;
    }

    private PlayerManager() {
    }

    private Player getPlayer(long playerId) {
        for (Player player: players) {
            if (player.getId() == playerId) {
                return player;
            }
        }
        return null;
    }

    public int getAllPlayersCount() {
        return players.size();
    }

    public long[] getAllPlayerIds() {
        long[] ids = new long[getAllPlayersCount()];
        for (int i = 0; i < getAllPlayersCount(); i++) {
            ids[i] = players.get(i).getId();
        }
        return ids;
    }

    public int getActivePlayersCount() {
        int playercount = 0;
        for (Player player: players) {
            if (!player.isDeleted()) {
                ++playercount;
            }
        }
        return playercount;
    }

    public long[] getActivePlayerIds() {
        long[] ids = new long[getActivePlayersCount()];
        int i = 0;
        for (Player player: players) {
            if (!player.isDeleted()) {
                ids[i] = player.getId();
                ++i;
            }
        }
        return ids;
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

    public void addPlayer(String name, String shortName) throws IllegalArgumentException {
        long id = System.currentTimeMillis();
        while (getPlayer(id) != null) {
            id = System.currentTimeMillis();
        }

        players.add(new Player(id, name, shortName));
    }

    public void editPlayer(long playerId, String name, String shortName) throws IllegalArgumentException {
        getPlayer(playerId).editPlayer(name, shortName);
    }

    public void deletePlayer(long playerId) {
        getPlayer(playerId).delete();
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

    public void replaceSelectedPlayers(long[] selectedPlayers) {
        this.selectedPlayers.clear();
        for(long playerId: selectedPlayers) {
            this.selectedPlayers.add(playerId);
        }
    }

    public void deselectDeletedPlayers() {
        ArrayList<Long> selectedPlayersCopy = (ArrayList<Long>) selectedPlayers.clone();
        for(long playerId: selectedPlayersCopy) {
            if (getPlayer(playerId).isDeleted()) {
                selectedPlayers.remove(playerId);
            }
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
            try {
                addPlayer("Test Player 1", "T1");
                addPlayer("Test Player 2", "T2");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
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
