package io.github.thachillera.cardsscorekeeper.data.players;

class Player {
    private final long id;
    private String name;

    //max 3 chars
    private String shortName;

    private int wins = 0;
    private int losses = 0;

    Player(long id, String name, String shortName) {
        if (name.length() == 0 || shortName.length() == 0 || shortName.length() > 3) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }

    long getId() {
        return id;
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
