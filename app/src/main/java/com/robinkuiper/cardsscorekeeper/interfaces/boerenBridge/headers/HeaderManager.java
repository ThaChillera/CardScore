package com.robinkuiper.cardsscorekeeper.interfaces.boerenBridge.headers;

import com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.ReadOnlyGameScoreManager;

import java.util.List;
import java.util.Map;

public class HeaderManager {
    private final List<PlayerHeader> playerHeaders;
    private final ReadOnlyGameScoreManager gameScoreManager;

    public HeaderManager(List<PlayerHeader> playerHeaders, ReadOnlyGameScoreManager gameScoreManager) {
        this.playerHeaders = playerHeaders;
        this.gameScoreManager = gameScoreManager;
    }

    public void updateScores() {
        Map<Long, Integer> results = gameScoreManager.getResults();
        for (PlayerHeader playerHeader: playerHeaders) {
            playerHeader.setPlayerScoreView(results.get(playerHeader.getPlayerID()));
        }
    }
}
