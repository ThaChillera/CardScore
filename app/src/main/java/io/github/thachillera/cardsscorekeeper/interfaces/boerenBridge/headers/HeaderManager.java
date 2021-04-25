package io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge.headers;

import java.util.List;
import java.util.Map;

import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.ReadOnlyGameScoreManager;

public class HeaderManager {
    private final List<PlayerHeader> playerHeaders;
    private final ReadOnlyGameScoreManager gameScoreManager;

    public HeaderManager(List<PlayerHeader> playerHeaders, ReadOnlyGameScoreManager gameScoreManager) {
        this.playerHeaders = playerHeaders;
        this.gameScoreManager = gameScoreManager;
    }

    public void updateScores() {
        Map<Long, Integer> results = gameScoreManager.getResults();
        for (PlayerHeader playerHeader : playerHeaders) {
            playerHeader.setPlayerScoreView(results.get(playerHeader.getPlayerID()));
        }
    }
}
