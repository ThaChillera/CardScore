package io.github.thachillera.cardsscorekeeper.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.GameScoreManager;
import io.github.thachillera.cardsscorekeeper.data.players.PlayerManager;

public class PersistenceManager {

    private static PersistenceManager instance = new PersistenceManager();

    private PersistenceManager() { }

    public static PersistenceManager getInstance() {
        return instance;
    }

    private Context context;

    public void setContext(Context context) {
        if (this.context == null) {
            this.context = context;
        }
    }

    /**
     * save data to location
     * @param data data to save
     * @param location place to save data
     * @throws IOException
     */
    private void save(String data, String location) throws IOException {
        FileOutputStream outputStream = context.openFileOutput(location, Context.MODE_PRIVATE);
        outputStream.write(data.getBytes());
        outputStream.close();
    }

    /**
     * load saved data from location
     * @param location location to load data from
     * @return loaded data
     * @throws IOException
     */
    private String load(String location) throws IOException {
        FileInputStream inputStream = context.openFileInput(location);
        byte[] buffer = new byte[(int)inputStream.getChannel().size()];
        inputStream.read(buffer);

        if (inputStream.available() > 0) {
            throw new IOException("still more to read");
        }

        return new String(buffer);
    }

    private final static String GAME_DATA_LOCATION = "gamedata.json";

    public boolean hasExistingSavedGame() {
        return new File(context.getFilesDir(),GAME_DATA_LOCATION).exists();
    }

    /**
     * Save game data
     * @param gameScoreManager gamescoremanager to save
     */
    public void saveGame(@NonNull GameScoreManager gameScoreManager) {
        try {
            save(gameScoreManager.getSaveGameData(), GAME_DATA_LOCATION);
        } catch (IOException exception) {
            Toast.makeText(context, "GameManager failed to save, some info could be lost", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Loads game data
     * @return saved GameScoreManager. If an error occurs, returns empty gamescoremanager
     */
    public GameScoreManager loadGame() {
        if (!hasExistingSavedGame()) {
            return new GameScoreManager(PlayerManager.getInstance().getSelectedPlayers());
        }

        try {
            return GameScoreManager.loadGameData(load(GAME_DATA_LOCATION));
        } catch (IOException exception) {
            Toast.makeText(context, "GameManager failed to load, some info could be lost", Toast.LENGTH_LONG).show();
            return new GameScoreManager(PlayerManager.getInstance().getSelectedPlayers());
        }
    }

    private final static String PLAYER_DATA_LOCATION = "playerData.json";

    public boolean hasExistingSavedPlayers() {
        return new File(context.getFilesDir(),PLAYER_DATA_LOCATION).exists();
    }

    /**
     * Saves player data
     */
    public void savePlayerData() {
        try {
            save(PlayerManager.getInstance().getSaveData(), PLAYER_DATA_LOCATION);
        } catch (IOException exception) {
            Toast.makeText(context, "PlayerManager failed to save, some info could be lost", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Loads player data
     * Automatically populates PlayerManager instance
     */
    public void loadPlayerData() {
        if (!hasExistingSavedPlayers()) {
            return;
        }

        try {
            PlayerManager.getInstance().loadPlayerData(load(PLAYER_DATA_LOCATION));
        } catch (IOException exception) {
            Toast.makeText(context, "PlayerManager failed to load, some info could be lost", Toast.LENGTH_LONG).show();
        }
    }
}
