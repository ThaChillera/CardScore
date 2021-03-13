package io.github.thachillera.cardsscorekeeper.data.players;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.github.thachillera.cardsscorekeeper.BuildConfig;
import io.github.thachillera.cardsscorekeeper.data.players.exceptions.DeletedPlayerException;
import io.github.thachillera.cardsscorekeeper.data.players.exceptions.DeselectPlayerException;
import io.github.thachillera.cardsscorekeeper.data.players.exceptions.InvalidPlayerIdException;
import io.github.thachillera.cardsscorekeeper.data.players.exceptions.NonExistantPlayerException;
import io.github.thachillera.cardsscorekeeper.data.players.exceptions.ReselectPlayerException;

public class PlayerManager {
    private static final String PLAYERDATALOCATION = "playerdata.json";
    private static PlayerManager ourInstance = new PlayerManager();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Long> selectedPlayers = new ArrayList<>();

    public static PlayerManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new PlayerManager();
        }
        return ourInstance;
    }

    private PlayerManager() {
    }

    public void addPlayer(String name, String shortName) throws IllegalArgumentException {
        long id = System.currentTimeMillis();
        while (getPlayer(id) != null) {
            id = System.currentTimeMillis();
        }

        players.add(new Player(id, name, shortName));
    }

    private boolean isValidId(long playerId) {
        return playerId > 0;
    }

    private boolean isActivePlayer(long playerId) {
        Player selectedPlayer = null;
        for(Player player: players) {
            if (player.getId() == playerId)
                selectedPlayer = player;
        }

        if (selectedPlayer == null) {
            throw new NonExistantPlayerException();
        }

        return !selectedPlayer.isDeleted();
    }

    /**
     * Validate player id input
     * Throws exceptions when invalid
     * @param playerId
     */
    private void validatePlayerIdInput(long playerId) {
        if (!isValidId(playerId)) {
            throw new InvalidPlayerIdException();
        } else if (getPlayer(playerId) == null) {
            throw new NonExistantPlayerException();
        }
    }

    public void editPlayer(long playerId, String name, String shortName) throws IllegalArgumentException {
        validatePlayerIdInput(playerId);
        getPlayer(playerId).editPlayer(name, shortName);
    }

    @Nullable
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

    public String getPlayerName(long playerId) {
        validatePlayerIdInput(playerId);
        return getPlayer(playerId).getName();
    }

    public String getPlayerShortName(long playerId) {
        validatePlayerIdInput(playerId);
        return getPlayer(playerId).getShortName();
    }

    public void deletePlayer(long playerId) {
        validatePlayerIdInput(playerId);

        if (isPlayerSelected(playerId)) {
            deselectPlayer(playerId);
        }
        getPlayer(playerId).delete();
    }

    public void selectPlayer(long playerId) {
        validatePlayerIdInput(playerId);
        if (!isActivePlayer(playerId)) {
            throw new DeletedPlayerException();
        } else if (isPlayerSelected(playerId)) {
            throw new ReselectPlayerException();
        } else {
            selectedPlayers.add(playerId);
        }
    }

    public void deselectPlayer(long playerId) {
        validatePlayerIdInput(playerId);
        if (!isActivePlayer(playerId)) {
            throw new DeletedPlayerException();
        }
        if (isPlayerSelected(playerId)) {
            selectedPlayers.remove(playerId);
        } else {
            throw new DeselectPlayerException();
        }
    }

    /**
     * Validate array of players parameter
     * Throws exception when invalid
     *
     * @param selectedPlayers
     */
    private void validatePlayerIdArrayInput(long[] selectedPlayers) {
        if (selectedPlayers == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        //check for duplicates
        Set<Long> foundPlayers = new HashSet<>();
        for(long player: selectedPlayers) {
            validatePlayerIdInput(player);

            if (foundPlayers.contains(player)) {
                throw new IllegalArgumentException("Duplicate player");
            }

            foundPlayers.add(player);
        }
    }

    public void replaceSelectedPlayers(long[] selectedPlayers) {
        validatePlayerIdArrayInput(selectedPlayers);

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
        validatePlayerIdInput(playerId);
        return selectedPlayers.contains(playerId);
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

    public String getSaveData() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(players.toArray());
    }

    public void loadPlayerData(String data) {
        players = new ArrayList<>(Arrays.asList(new Gson().fromJson(data, Player[].class)));

        //Junk Players
        if (BuildConfig.DEBUG && players.isEmpty()) {
            try {
                for (int i = 0; i < 5; i++) {
                    addPlayer("Test Player " + i, "T" + i);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
