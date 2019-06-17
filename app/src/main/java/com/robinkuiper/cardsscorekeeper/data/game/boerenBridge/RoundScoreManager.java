package com.robinkuiper.cardsscorekeeper.data.game.boerenBridge;

import android.widget.TextView;

import java.util.ArrayList;

public class RoundScoreManager {
    private ArrayList<TextView> textViews = new ArrayList<>();
    private final GameScoreManager PARENT;
    public final RoundScoreManagerType TYPE;

    public RoundScoreManager(GameScoreManager PARENT, RoundScoreManagerType TYPE) {
        this.PARENT = PARENT;
        this.TYPE = TYPE;
    }

    public void addTextView(TextView view) {
        textViews.add(view);
    }

    public void enterScores(int[] scores) {
        for (int i = 0; i < scores.length; i++) {
            textViews.get(i).setText(Integer.toString(scores[i]));
        }
        PARENT.enterScores(scores, TYPE);
    }
}
