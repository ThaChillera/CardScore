package io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;

import io.github.thachillera.cardsscorekeeper.R;
import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.GameScoreManager;
import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.ReadOnlyGameScoreManager;
import io.github.thachillera.cardsscorekeeper.data.players.PlayerManager;
import io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge.headers.HeaderManager;
import io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge.headers.PlayerHeader;
import io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge.rows.RowManager;
import io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge.rows.ScoreCard;

import java.util.ArrayList;
import java.util.Collections;

public class BoerenBridge extends AppCompatActivity {

    final private String TAG = "BoerenBridge";

    private final PlayerManager playerManager = PlayerManager.getInstance();
    private final GameScoreManager gameScoreManager = new GameScoreManager(playerManager.getSelectedPlayerCount());

    private HeaderManager headerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boeren_bridge);

        GridLayout grid = findViewById(R.id.boeren_bridge_gridlayout);

        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, this.getResources().getDisplayMetrics());
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, this.getResources().getDisplayMetrics());

        LayoutParams params = new LayoutParams((int) width, (int) height);

        //add players
        grid.setColumnCount(playerManager.getSelectedPlayerCount() + 1);

        //add player headers
        ArrayList<PlayerHeader> playerHeaders = new ArrayList<>();
        for (long playerID: playerManager.getSelectedPlayers()) {
            PlayerHeader playerHeader = new PlayerHeader(this, playerManager.getPlayerName(playerID), playerID);
            playerHeader.setLayoutParams(params);
            grid.addView(playerHeader);
            playerHeaders.add(playerHeader);
        }
        headerManager = new HeaderManager(Collections.unmodifiableList(playerHeaders), gameScoreManager);

        //add round + scores
        for (int rounds = 1; rounds < gameScoreManager.getAmountOfRounds() + 1; rounds++) {
            //create player round info
            ArrayList<ScoreCard> scoreCards = new ArrayList<>();
            for (long playerID: playerManager.getSelectedPlayers()) {
                ScoreCard sc = new ScoreCard(this, playerID);
                sc.setLayoutParams(params);
                scoreCards.add(sc);
            }

            //add general round info
            int cardCount = rounds < gameScoreManager.getMaxCards() ? rounds: gameScoreManager.getMaxCards() - (rounds - gameScoreManager.getMaxCards());
            RoundCount rc = new RoundCount(this,
                    gameScoreManager,
                    headerManager,
                    new RowManager(rounds - 1, scoreCards, gameScoreManager),
                    rounds, cardCount);
            rc.setLayoutParams(params);
            grid.addView(rc);

            //add player round info
            for (ScoreCard card: scoreCards) {
                grid.addView(card);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        PlayerManager.getInstance().savePlayerData(this);
    }
}
