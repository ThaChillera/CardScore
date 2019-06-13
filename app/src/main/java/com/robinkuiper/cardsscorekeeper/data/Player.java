package com.robinkuiper.cardsscorekeeper.data;

import java.util.ArrayList;

class Player {
    private int id;
    private String name;

    //max 3 chars
    private String shortName;

    private int wins = 0;
    private int losses = 0;

    //scores for current game
    private ArrayList<Integer> predictedScores = new ArrayList<>();
    private ArrayList<Integer> enteredScores = new ArrayList<>();

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

    void addPrediction(int score) {
        predictedScores.add(score);
    }

    void addScore(int score) {
        enteredScores.add(score);
    }

    int getScore() {
        int score = 0;
        for (int i = 0; i < enteredScores.size(); i++) {
            if (predictedScores.get(i).equals(enteredScores.get(i))) {
                score += 10 + (enteredScores.get(i) * 2);
            } else {
                score -= Math.abs(enteredScores.get(i) - predictedScores.get(i)) * 2;
            }
        }

        return score;
    }

    void editPlayer(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    void reset() {
        predictedScores = new ArrayList<>();
        enteredScores = new ArrayList<>();
    }
}
