package com.robinkuiper.cardsscorekeeper.interfaces.boerenBridge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;

import com.robinkuiper.cardsscorekeeper.R;
import com.robinkuiper.cardsscorekeeper.data.players.PlayerManager;

import java.util.ArrayList;

public class BoerenBridge extends AppCompatActivity {

    final private String TAG = "BoerenBridge";

    private PlayerManager playerManager = PlayerManager.getInstance();
    private int[] selectedPlayers = playerManager.getSelectedPlayers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boeren_bridge);

        GridLayout grid = findViewById(R.id.boeren_bridge_gridlayout);

        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, this.getResources().getDisplayMetrics());
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, this.getResources().getDisplayMetrics());

        LayoutParams params = new LayoutParams((int) width, (int) height);

        //add players
        grid.setColumnCount(selectedPlayers.length + 1);

        ArrayList<PlayerHeader> playerHeaders = new ArrayList<>();
        for (int index: selectedPlayers) {
            //add gplayer headers
            PlayerHeader playerHeader = new PlayerHeader(this, playerManager.getPlayerName(index));
            playerHeader.setLayoutParams(params);
            grid.addView(playerHeader);
            playerHeaders.add(playerHeader);
        }

        //add round + scores
        int maxCards = 52 % selectedPlayers.length == 0 ? (52 - selectedPlayers.length) / selectedPlayers.length : 52 / selectedPlayers.length;
        int amountOfRounds = ((maxCards * 2)) - 1;

        GameScoreManager gameScoreManager = new GameScoreManager(amountOfRounds, playerHeaders);

        for (int rounds = 1; rounds < amountOfRounds + 1; rounds++) {
            //add general round info
            int cardCount = rounds < maxCards ? rounds: maxCards - (rounds - maxCards);
            RoundCount rc = new RoundCount(this,
                    gameScoreManager.getPredictionRoundScoreManager(rounds - 1),
                    gameScoreManager.getEnterRoundScoreManager(rounds - 1),
                    rounds, cardCount);
            rc.setLayoutParams(params);
            grid.addView(rc);

            //add player round info
            for (int i = 0; i < selectedPlayers.length; i++) {
                ScoreCard sc = new ScoreCard(this,
                        gameScoreManager.getPredictionRoundScoreManager(rounds - 1),
                        gameScoreManager.getEnterRoundScoreManager(rounds - 1));
                sc.setLayoutParams(params);
                grid.addView(sc);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        PlayerManager.getInstance().savePlayerData(this);
    }
}
