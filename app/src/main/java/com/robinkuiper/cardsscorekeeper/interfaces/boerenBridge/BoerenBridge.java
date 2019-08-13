package com.robinkuiper.cardsscorekeeper.interfaces.boerenBridge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;

import com.robinkuiper.cardsscorekeeper.R;
import com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.GameScoreManager;
import com.robinkuiper.cardsscorekeeper.data.game.boerenBridge.ReadOnlyGameScoreManager;
import com.robinkuiper.cardsscorekeeper.data.players.PlayerManager;
import com.robinkuiper.cardsscorekeeper.interfaces.boerenBridge.headers.HeaderManager;
import com.robinkuiper.cardsscorekeeper.interfaces.boerenBridge.headers.PlayerHeader;
import com.robinkuiper.cardsscorekeeper.interfaces.boerenBridge.rows.RowManager;
import com.robinkuiper.cardsscorekeeper.interfaces.boerenBridge.rows.ScoreCard;

import java.util.ArrayList;
import java.util.Collections;

public class BoerenBridge extends AppCompatActivity {

    final private String TAG = "BoerenBridge";
    final static public String LOADSAVEGAMEEXTRA = "LoadSaveGame";

    private final PlayerManager playerManager = PlayerManager.getInstance();
    private GameScoreManager gameScoreManager;

    private HeaderManager headerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load save if relevant
        boolean loadSave = getIntent().getBooleanExtra(LOADSAVEGAMEEXTRA, false);
        if (loadSave) {
            gameScoreManager = ReadOnlyGameScoreManager.loadGameData(getApplicationContext());
        } else {
            gameScoreManager = new GameScoreManager(getApplicationContext(), PlayerManager.getInstance().getSelectedPlayerCount());
        }

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

        ArrayList<RowManager> rowManagers = new ArrayList<>();
        ArrayList<RoundCount> roundCounts = new ArrayList<>();

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
            RowManager rowManager = new RowManager(rounds - 1, scoreCards, gameScoreManager);
            RoundCount rc = new RoundCount(this,
                    gameScoreManager,
                    headerManager,
                    rowManager,
                    rounds, cardCount);

            rc.setLayoutParams(params);
            grid.addView(rc);

            //add player round info
            for (ScoreCard card: scoreCards) {
                grid.addView(card);
            }

            //save objects for save loading
            rowManagers.add(rowManager);
            roundCounts.add(rc);
        }

        //load game data into fields
        if (loadSave) {
            headerManager.updateScores();

            for (int i = 0; i < gameScoreManager.getRound(); i++) {
                RowManager rowManager = rowManagers.get(i);
                rowManager.updatePredictions();
                rowManager.updateScores();

                roundCounts.get(i).changeButtonVisibility(false);
            }

            //display prediction
            if (gameScoreManager.getNextEntry() == ReadOnlyGameScoreManager.EntryType.SCORE) {
                rowManagers.get(gameScoreManager.getRound()).updatePredictions();

                roundCounts.get(gameScoreManager.getRound()).changeButtonVisibility(true);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        gameScoreManager.saveGameData();
        PlayerManager.getInstance().savePlayerData(this);
    }
}
