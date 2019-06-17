package com.robinkuiper.cardsscorekeeper.data.players;

import java.util.ArrayList;

class Player {
    private int id;
    private String name;

    //max 3 chars
    private String shortName;

    private int wins = 0;
    private int losses = 0;

    Player(String name, String shortName) {
        if (name.length() == 0 || shortName.length() == 0 || shortName.length() > 3) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.shortName = shortName;
    }

    String getName() {
        return name;
    }

    String getShortName() {
        return shortName;
    }

    void editPlayer(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }
}
