package com.robinkuiper.cardsscorekeeper.data;

import java.util.ArrayList;

class Player {
    private String name;
    private int wins;
    private int losses;

    //scores for current game
    private ArrayList<Integer> predictedScores = new ArrayList<>();
    private ArrayList<Integer> enteredScores = new ArrayList<>();

    Player(String name) {
        this.name = name;
        wins = 0;
        losses = 0;
    }

    String getName() {
        return name;
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

    void reset() {
        predictedScores = new ArrayList<>();
        enteredScores = new ArrayList<>();
    }
}
