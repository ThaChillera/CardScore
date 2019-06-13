package com.robinkuiper.cardsscorekeeper.boerenBridge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.TextView;

import com.robinkuiper.cardsscorekeeper.R;
import com.robinkuiper.cardsscorekeeper.data.PlayerManager;

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

    enum RoundScoreManagerType {
        PREDICTIONS, SCORES
    }

    class GameScoreManager {
        private RoundScoreManager[] predictions, scores;
        private ArrayList<PlayerHeader> playerHeaders;

        GameScoreManager(int rounds, ArrayList<PlayerHeader> playerHeaders) {
            this.playerHeaders = playerHeaders;
            predictions = new RoundScoreManager[rounds];
            scores = new RoundScoreManager[rounds];

            for (int i = 0; i < rounds; i++) {
                predictions[i] = new RoundScoreManager(this, RoundScoreManagerType.PREDICTIONS);
                scores[i] = new RoundScoreManager(this, RoundScoreManagerType.SCORES);
            }
        }

        RoundScoreManager getPredictionRoundScoreManager(int i) {
            return predictions[i];
        }

        RoundScoreManager getEnterRoundScoreManager(int i) {
            return scores[i];
        }

        void enterScores(int[] scores, RoundScoreManagerType type) {
            for (int i = 0; i < scores.length; i++) {
                if (type == RoundScoreManagerType.PREDICTIONS) {
                    playerManager.addPlayerPrediction(selectedPlayers[i], scores[i]);
                } else {
                    playerManager.addPlayerScore(selectedPlayers[i], scores[i]);
                }

                playerHeaders.get(i).setPlayerScoreView(playerManager.getPlayerScore(selectedPlayers[i]));
            }
        }
    }

    class RoundScoreManager {
        private ArrayList<TextView> textViews = new ArrayList<>();
        private GameScoreManager parent;
        RoundScoreManagerType type;

        public RoundScoreManager(GameScoreManager parent, RoundScoreManagerType type) {
            this.parent = parent;
            this.type = type;
        }

        void addTextView(TextView view) {
            textViews.add(view);
        }

        void enterScores(int[] scores) {
            for (int i = 0; i < scores.length; i++) {
                textViews.get(i).setText(Integer.toString(scores[i]));
            }
            parent.enterScores(scores, type);
        }
    }
}
