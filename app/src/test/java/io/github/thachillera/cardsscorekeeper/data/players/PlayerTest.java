package io.github.thachillera.cardsscorekeeper.data.players;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.Nullable;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class PlayerTest {

    public static final int ID = 0;
    public static final String NAME = "Player Name";
    public static final String SHORTNAME = "SN";

    public static final String EDITEDNAME = "Edited Player Name";
    public static final String EDITEDSHORTNAME = "SN2";

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

    public static final String INVALIDSHORTNAME = "Four";

    @Test(expected = IllegalArgumentException.class)
    @Parameters({"null," + SHORTNAME,
                "," + SHORTNAME,
                NAME + ",null",
                NAME + ",",
                NAME + "," + INVALIDSHORTNAME})
    public void createPlayerBadNames(@Nullable String name, @Nullable String shortName) {
        new Player(ID, name, shortName);
    }

    @Test(expected = IllegalArgumentException.class)
    @Parameters({"null," + EDITEDSHORTNAME,
            "," + EDITEDSHORTNAME,
            EDITEDNAME + ",null",
            EDITEDNAME + ",",
            EDITEDNAME + "," + INVALIDSHORTNAME})
    public void editPlayerBadNames(@Nullable String name, @Nullable String shortName) {
        player.editPlayer(name, shortName);
    }
}