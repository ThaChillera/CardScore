package io.github.thachillera.cardsscorekeeper.data.players;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import io.github.thachillera.cardsscorekeeper.data.players.exceptions.InvalidPlayerIdException;
import io.github.thachillera.cardsscorekeeper.data.players.exceptions.NonExistantPlayerException;

import static org.junit.Assert.*;

public class PlayerManagerTest {

    PlayerManager playerManager;

    public static final int PLAYERAMOUNT = 7;
    public static final String[] NAMES = {PlayerTest.NAME + 0, PlayerTest.NAME + 1, PlayerTest.NAME + 2, PlayerTest.NAME + 3, PlayerTest.NAME + 4,
            PlayerTest.NAME + 5, PlayerTest.NAME + 6, PlayerTest.NAME + 7};
    public static final String[] SHORTNAMES = {PlayerTest.SHORTNAME + 0, PlayerTest.SHORTNAME + 1, PlayerTest.SHORTNAME + 2, PlayerTest.SHORTNAME + 3,
            PlayerTest.SHORTNAME + 4, PlayerTest.SHORTNAME + 5, PlayerTest.SHORTNAME + 6, PlayerTest.SHORTNAME + 7};

    @Before
    public void setUp() {
        playerManager = PlayerManager.getInstance();

        for (int i = 0; i < PLAYERAMOUNT; i++) {
            playerManager.addPlayer(NAMES[i], SHORTNAMES[i]);
        }
    }

    @After
    public void tearDown() throws Exception {
        //remove instance
        Field instance = PlayerManager.class.getDeclaredField("ourInstance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    /**
     * Goodweather tests
     */

    @Test
    public void getInstance() {
        assertNotNull(PlayerManager.getInstance());
    }

    private boolean containsPlayer(String name, String shortName) {
        long[] ids = playerManager.getAllPlayerIds();
        for (long id: ids) {
            if (playerManager.getPlayerName(id).equals(name) && playerManager.getPlayerShortName(id).equals(shortName)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void addPlayer() {
        playerManager.addPlayer(PlayerTest.NAME + PLAYERAMOUNT, PlayerTest.SHORTNAME + PLAYERAMOUNT);
        assertTrue(containsPlayer(PlayerTest.NAME + PLAYERAMOUNT, PlayerTest.SHORTNAME + PLAYERAMOUNT));
    }

    @Test
    public void editPlayer() {
        long playerId = playerManager.getActivePlayerIds()[0];
        playerManager.editPlayer(playerId, PlayerTest.NAME + PLAYERAMOUNT, PlayerTest.SHORTNAME + PLAYERAMOUNT);
        assertTrue(containsPlayer(PlayerTest.NAME + PLAYERAMOUNT, PlayerTest.SHORTNAME + PLAYERAMOUNT));
    }

    @Test
    public void getAllPlayersCount() {
        assertEquals(PLAYERAMOUNT, playerManager.getAllPlayersCount());

        playerManager.deletePlayer(playerManager.getAllPlayerIds()[0]);
        assertEquals(PLAYERAMOUNT, playerManager.getAllPlayersCount());
    }

    @Test
    public void getAllPlayerIds() {
        long[] players = playerManager.getAllPlayerIds();
        assertEquals(PLAYERAMOUNT, players.length);

        for(long player: players) {
            assertTrue(player > 0);
        }

        playerManager.deletePlayer(playerManager.getAllPlayerIds()[0]);
        assertEquals(PLAYERAMOUNT, playerManager.getAllPlayersCount());
    }

    @Test
    public void getActivePlayersCount() {
        assertEquals(PLAYERAMOUNT, playerManager.getActivePlayersCount());

        playerManager.deletePlayer(playerManager.getActivePlayerIds()[0]);
        assertEquals(PLAYERAMOUNT - 1, playerManager.getActivePlayersCount());

        playerManager.addPlayer(NAMES[PLAYERAMOUNT], SHORTNAMES[PLAYERAMOUNT]);
        assertEquals(PLAYERAMOUNT, playerManager.getActivePlayersCount());
    }

    @Test
    public void getActivePlayerIds() {
        long[] players = playerManager.getActivePlayerIds();
        assertEquals(PLAYERAMOUNT, players.length);

        for(long player: players) {
            assertTrue(player > 0);
        }

        playerManager.deletePlayer(playerManager.getActivePlayerIds()[0]);
        assertEquals(PLAYERAMOUNT - 1, playerManager.getActivePlayerIds().length);
    }

    @Test
    public void getPlayerName() {
        assertEquals(NAMES[0], playerManager.getPlayerName(playerManager.getAllPlayerIds()[0]));
    }

    @Test
    public void getPlayerShortName() {
        assertEquals(SHORTNAMES[0], playerManager.getPlayerShortName(playerManager.getAllPlayerIds()[0]));
    }

    @Test
    public void selectPlayer() {
        long selectedPlayer = playerManager.getActivePlayerIds()[0];
        playerManager.selectPlayer(selectedPlayer);
        long[] selectedPlayers = playerManager.getSelectedPlayers();
        assertEquals("More players than supposed selected", 1, selectedPlayers.length);
        assertEquals("Other player selected", selectedPlayer, selectedPlayers[0]);

        playerManager.deletePlayer(selectedPlayer);
        assertEquals("Deleted player still selected", 0, playerManager.getSelectedPlayers().length);
    }

    @Test
    public void deselectPlayer() {
        long selectedPlayer = playerManager.getActivePlayerIds()[0];
        playerManager.selectPlayer(selectedPlayer);
        playerManager.deselectPlayer(selectedPlayer);
        assertEquals("Player not deselected" ,0, playerManager.getSelectedPlayers().length);

        playerManager.deselectPlayer(selectedPlayer);
        assertEquals("Player deselect fails on second deselect", 0, playerManager.getSelectedPlayers().length);
    }

    @Test
    public void replaceSelectedPlayers() {
        long[] players = playerManager.getActivePlayerIds();
        long[] toBeSelectedPlayers = Arrays.copyOfRange(players, 1, players.length);

        playerManager.selectPlayer(players[0]);
        playerManager.selectPlayer(players[1]);

        playerManager.replaceSelectedPlayers(toBeSelectedPlayers);
        assertArrayEquals(toBeSelectedPlayers, playerManager.getSelectedPlayers());
    }

    @Test
    public void deselectDeletedPlayers() {
    }

    @Test
    public void isPlayerSelected() {
        long selectedPlayer = playerManager.getActivePlayerIds()[0];
        playerManager.selectPlayer(selectedPlayer);
        assertTrue(playerManager.isPlayerSelected(selectedPlayer));
        playerManager.deselectPlayer(selectedPlayer);
        assertFalse(playerManager.isPlayerSelected(selectedPlayer));
    }

    @Test
    public void getSelectedPlayerCount() {
        final int SELECTEDPLAYERCOUNT = 3;
        long[] players = playerManager.getActivePlayerIds();
        for (int i = 0; i < SELECTEDPLAYERCOUNT; i++) {
            playerManager.selectPlayer(players[i]);
        }

        assertEquals(SELECTEDPLAYERCOUNT, playerManager.getSelectedPlayerCount());
    }

    /**
     * Badweather Tests
     */

    @Test(expected = IllegalArgumentException.class)
    public void addPlayerNullName() {
        playerManager.addPlayer(null, PlayerTest.SHORTNAME + PLAYERAMOUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPlayerEmptyName() {
        playerManager.addPlayer("", PlayerTest.SHORTNAME + PLAYERAMOUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPlayerNullShortName() {
        playerManager.addPlayer(PlayerTest.NAME + PLAYERAMOUNT, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPlayerEmptyShortName() {
        playerManager.addPlayer(PlayerTest.NAME + PLAYERAMOUNT, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPlayerLongShortName() {
        playerManager.addPlayer(PlayerTest.NAME + PLAYERAMOUNT, PlayerTest.INVALIDSHORTNAME);
    }

    private static final long NONEXISTINGPLAYER = 1;

    boolean existingPlayerId(long id) {
        return Arrays.asList(playerManager.getAllPlayerIds()).contains(id);
    }

    @Test(expected = InvalidPlayerIdException.class)
    public void editPlayerInvalidId() {
        playerManager.editPlayer(-1, PlayerTest.NAME + PLAYERAMOUNT, PlayerTest.SHORTNAME + PLAYERAMOUNT);
    }

    @Test(expected = NonExistantPlayerException.class)
    public void editPlayerNonExistingPlayerId() {
        assertFalse(existingPlayerId(NONEXISTINGPLAYER));
        playerManager.editPlayer(NONEXISTINGPLAYER, PlayerTest.NAME + PLAYERAMOUNT, PlayerTest.SHORTNAME + PLAYERAMOUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void editPlayerNullName() {
        long playerId = playerManager.getActivePlayerIds()[0];
        playerManager.editPlayer(playerId, null, PlayerTest.SHORTNAME + PLAYERAMOUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void editPlayerEmptyName() {
        long playerId = playerManager.getActivePlayerIds()[0];
        playerManager.editPlayer(playerId, "", PlayerTest.SHORTNAME + PLAYERAMOUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void editPlayerNullShortName() {
        long playerId = playerManager.getActivePlayerIds()[0];
        playerManager.editPlayer(playerId, PlayerTest.NAME + PLAYERAMOUNT, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void editPlayerEmptyShortName() {
        long playerId = playerManager.getActivePlayerIds()[0];
        playerManager.editPlayer(playerId, PlayerTest.NAME + PLAYERAMOUNT, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void editPlayerLongShortName() {
        long playerId = playerManager.getActivePlayerIds()[0];
        playerManager.editPlayer(playerId, PlayerTest.NAME + PLAYERAMOUNT, PlayerTest.INVALIDSHORTNAME);
    }

    @Test(expected = InvalidPlayerIdException.class)
    public void getPlayerNameInvalidId() {
        playerManager.getPlayerName(-1);
    }

    @Test(expected = NonExistantPlayerException.class)
    public void getPlayerNameNonExistingPlayerId() {
        assertFalse(existingPlayerId(NONEXISTINGPLAYER));
        playerManager.getPlayerName(NONEXISTINGPLAYER);
    }

    @Test(expected = InvalidPlayerIdException.class)
    public void getPlayerShortNameInvalidId() {
        playerManager.getPlayerShortName(-1);
    }

    @Test(expected = NonExistantPlayerException.class)
    public void getPlayerShortNameNonExistingPlayerId() {
        assertFalse(existingPlayerId(NONEXISTINGPLAYER));
        playerManager.getPlayerShortName(NONEXISTINGPLAYER);
    }

    @Test(expected = InvalidPlayerIdException.class)
    public void deletePlayerInvalidId() {
        playerManager.deletePlayer(-1);
    }

    @Test(expected = NonExistantPlayerException.class)
    public void deletePlayerNonExistingPlayerId() {
        assertFalse(existingPlayerId(NONEXISTINGPLAYER));
        playerManager.deletePlayer(NONEXISTINGPLAYER);
    }

    @Test(expected = InvalidPlayerIdException.class)
    public void selectPlayerInvalidId() {
        playerManager.selectPlayer(-1);
    }

    @Test(expected = NonExistantPlayerException.class)
    public void selectPlayerNonExistingPlayerId() {
        assertFalse(existingPlayerId(NONEXISTINGPLAYER));
        playerManager.selectPlayer(NONEXISTINGPLAYER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectPlayerAlreadySelected() {
        long playerId = playerManager.getActivePlayerIds()[0];
        playerManager.selectPlayer(playerId);
        playerManager.selectPlayer(playerId);
    }

    @Test(expected = IllegalArgumentException.class) //figure out what exception to throw
    public void selectPlayerInactivePlayer() {
        long playerId = playerManager.getActivePlayerIds()[0];
        playerManager.deletePlayer(playerId);
        playerManager.selectPlayer(playerId);
    }

    @Test(expected = InvalidPlayerIdException.class)
    public void deselectPlayerInvalidId() {
        playerManager.deselectPlayer(-1);
    }

    @Test(expected = NonExistantPlayerException.class)
    public void deselectPlayerNonExistingPlayerId() {
        assertFalse(existingPlayerId(NONEXISTINGPLAYER));
        playerManager.deselectPlayer(NONEXISTINGPLAYER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deselectPlayerNotSelected() {
        long playerId = playerManager.getActivePlayerIds()[0];
        playerManager.deselectPlayer(playerId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deselectPlayerInactivePlayer() {
        long playerId = playerManager.getActivePlayerIds()[0];
        playerManager.deletePlayer(playerId);
        playerManager.deselectPlayer(playerId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void replaceSelectedPlayersNullArray() {
        playerManager.replaceSelectedPlayers(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void replaceSelectedPlayersInvalidPlayerArray() {
        long[] invalidArray = {NONEXISTINGPLAYER};
        playerManager.replaceSelectedPlayers(invalidArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void replaceSelectedPlayersDuplicatePlayerArray() {
        long playerId = playerManager.getActivePlayerIds()[0];
        long[] invalidArray = {playerId, playerId};
        playerManager.replaceSelectedPlayers(invalidArray);
    }

    @Test(expected = InvalidPlayerIdException.class)
    public void isPlayerSelectedInvalidId() {
        playerManager.isPlayerSelected(-1);
    }

    @Test(expected = NonExistantPlayerException.class)
    public void isPlayerSelectedNonExistingPlayerId() {
        assertFalse(existingPlayerId(NONEXISTINGPLAYER));
        playerManager.isPlayerSelected(NONEXISTINGPLAYER);
    }
}