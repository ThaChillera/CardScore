package io.github.thachillera.cardsscorekeeper.data.players;

class Player {
    private final long id;
    private String name;

    //max 3 chars
    private String shortName;

    private boolean deleted = false;

    private int wins = 0;
    private int losses = 0;

    Player(long id, String name, String shortName) throws IllegalArgumentException {
        this.id = id;
        setName(name);
        setShortName(shortName);
    }

    long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    private void setName(String name) throws IllegalArgumentException {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("No or missing name");
        }

        this.name = name;
    }

    String getShortName() {
        return shortName;
    }

    private void setShortName(String shortName) throws IllegalArgumentException {
        if (shortName == null || shortName.length() == 0 || shortName.length() > 3) {
            throw new IllegalArgumentException("No, missing or too long name");
        }

        this.shortName = shortName;
    }

    void editPlayer(String name, String shortName) throws IllegalArgumentException {
        setName(name);
        setShortName(shortName);
    }

    boolean isDeleted() {
        return deleted;
    }

    void delete() {
        deleted = true;
    }
}
