package io.github.thachillera.cardsscorekeeper.data.players;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class PlayerTest {

    private final int ID = 0;
    private final String NAME = "Player Name";
    private final String SHORTNAME = "PSN";

    private final String EDITEDNAME = "Edited Player Name";
    private final String EDITEDSHORTNAME = "SN2";

    Player player;

    @Before
    public void setUp() throws Exception {
         player = new Player(ID, NAME, SHORTNAME);
    }

    /**
     * Goodweather tests
     */

    @Test
    public void getId() {
        assertEquals(ID, player.getId());
    }

    @Test
    public void getName() {
        assertEquals(NAME, player.getName());
    }

    @Test
    public void getShortName() {
        assertEquals(SHORTNAME, player.getShortName());
    }

    @Test
    public void editPlayer() {
        player.editPlayer(EDITEDNAME, EDITEDSHORTNAME);
        assertEquals(player.getName(), EDITEDNAME);
        assertEquals(player.getShortName(), EDITEDSHORTNAME);
    }

    @Test
    public void isDeleted() {
        assertFalse(player.isDeleted());
        player.delete();
        assertTrue(player.isDeleted());
    }

    /**
     * Badweather tests
     */

    private final String INVALIDSHORTNAME = "Four";

    @Test(expected = IllegalArgumentException.class)
    public void createPlayerNoName() {
        new Player(ID, null, SHORTNAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createPlayerEmptyName() {
        new Player(ID, "", SHORTNAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createPlayerNoShortName() {
        new Player(ID, NAME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createPlayerEmptyShortName() {
        new Player(ID, NAME, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createPlayerLongShortName() {
        new Player(ID, NAME, INVALIDSHORTNAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void editPlayerNoName() {
        player.editPlayer(null, EDITEDSHORTNAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void editPlayerEmptyName() {
        player.editPlayer("", EDITEDSHORTNAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void editPlayerNoShortName() {
        player.editPlayer(EDITEDNAME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void editPlayerEmptyShortName() {
        player.editPlayer(EDITEDNAME, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void editPlayerLongShortName() {
        player.editPlayer(EDITEDNAME, INVALIDSHORTNAME);
    }
}