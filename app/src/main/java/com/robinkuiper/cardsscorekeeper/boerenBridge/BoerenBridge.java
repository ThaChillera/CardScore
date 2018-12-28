package com.robinkuiper.cardsscorekeeper.boerenBridge;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robinkuiper.cardsscorekeeper.R;
import com.robinkuiper.cardsscorekeeper.data.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class BoerenBridge extends AppCompatActivity {

    final private String TAG = "BoerenBridge";
    final private String PLAYERDATALOCATION = "playerdata.json";

    ArrayList<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            FileInputStream inputStream = getApplicationContext().openFileInput(PLAYERDATALOCATION);
            players = new ArrayList<>(Arrays.asList(new Gson().fromJson(new InputStreamReader(inputStream), Player[].class)));

        } catch (IOException exception) {
            Log.e(TAG, "onCreate: ", exception);
            players = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                players.add(new Player("Player " + (i + 1)));
            }
        }

        setContentView(R.layout.activity_boeren_bridge);

        GridLayout grid = findViewById(R.id.boeren_bridge_gridlayout);

        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, this.getResources().getDisplayMetrics());
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, this.getResources().getDisplayMetrics());

        LayoutParams params = new LayoutParams((int) width, (int) height);

        //add players
        grid.setColumnCount(players.size() + 1);

        ArrayList<PlayerHeader> playerHeaders = new ArrayList<>();
        for (Player player : players) {
            //add gplayer headers
            PlayerHeader playerHeader = new PlayerHeader(this, player.getName(), players.indexOf(player) + 1);
            playerHeader.setLayoutParams(params);
            grid.addView(playerHeader);
            playerHeaders.add(playerHeader);
        }

        //add round + scores
        int maxCards = 52 % players.size() == 0 ? (52 - players.size()) / players.size() : 52 / players.size();
        int amountOfRounds = ((maxCards * 2)) - 1;

        GameScoreManager gameScoreManager = new GameScoreManager(amountOfRounds, players, playerHeaders);

        for (int rounds = 1; rounds < amountOfRounds + 1; rounds++) {
            //add general round info
            int cardCount = rounds < maxCards ? rounds: maxCards - (rounds - maxCards);
            RoundCount rc = new RoundCount(this,
                    gameScoreManager.getPredictionRoundScoreManager(rounds - 1),
                    gameScoreManager.getEnterRoundScoreManager(rounds - 1),
                    players.size(), rounds, cardCount);
            rc.setLayoutParams(params);
            grid.addView(rc);

            //add player round info
            for (Player player: players) {
                ScoreCard sc = new ScoreCard(this,
                        gameScoreManager.getPredictionRoundScoreManager(rounds - 1),
                        gameScoreManager.getEnterRoundScoreManager(rounds - 1));
                sc.setLayoutParams(params);
                grid.addView(sc);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            for (Player player: players) {
                player.reset();
            }

            FileOutputStream outputStream = getApplicationContext().openFileOutput(PLAYERDATALOCATION, Context.MODE_PRIVATE);
            outputStream.write(gson.toJson(players.toArray()).getBytes());
            outputStream.close();

        } catch (IOException exception) {
            Toast.makeText(this, "PlayerData failed to save, some info could be lost", Toast.LENGTH_LONG).show();
        }
    }

    enum RoundScoreManagerType {
        PREDICTIONS, SCORES
    }

    class GameScoreManager {
        private RoundScoreManager[] predictions, scores;
        private ArrayList<Player> players;
        private ArrayList<PlayerHeader> playerHeaders;

        GameScoreManager(int rounds, ArrayList<Player> players, ArrayList<PlayerHeader> playerHeaders) {
            this.players = players;
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
                    players.get(i).addPrediction(scores[i]);
                } else {
                    players.get(i).addScore(scores[i]);
                }

                playerHeaders.get(i).setPlayerScoreView(players.get(i).getScore());
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
