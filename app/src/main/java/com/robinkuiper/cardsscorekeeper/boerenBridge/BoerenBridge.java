package com.robinkuiper.cardsscorekeeper.boerenBridge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.TextView;

import com.robinkuiper.cardsscorekeeper.R;
import com.robinkuiper.cardsscorekeeper.data.Player;

import java.util.ArrayList;

public class BoerenBridge extends AppCompatActivity {

    final private String TAG = "BoerenBridge";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_boeren_bridge);

        GridLayout grid = findViewById(R.id.boeren_bridge_gridlayout);

        ArrayList<Player> players = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            players.add(new Player("Player " + (i + 1)));
        }

        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, this.getResources().getDisplayMetrics());
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, this.getResources().getDisplayMetrics());

        LayoutParams params = new LayoutParams((int) width, (int) height);

        //add players
        grid.setColumnCount(players.size() + 1);

        for (Player player : players) {
            //add gplayer headers
            PlayerHeader playerHeader = new PlayerHeader(this, player.getName(), players.indexOf(player) + 1);
            playerHeader.setLayoutParams(params);
            grid.addView(playerHeader);
        }

        //add round + scores
        int maxCards = 52 % players.size() == 0 ? (52 - players.size()) / players.size() : 52 / players.size();

        for (int rounds = 1; rounds < ((maxCards * 2)); rounds++) {
            //add general round info
            ScoreManager predictedScoreManager = new ScoreManager();
            ScoreManager enteredScoreManager = new ScoreManager();
            int cardCount = rounds < maxCards ? rounds: maxCards - (rounds - maxCards);
            RoundCount rc = new RoundCount(this, predictedScoreManager, enteredScoreManager, players.size(), rounds, cardCount);
            rc.setLayoutParams(params);
            grid.addView(rc);

            //add player round info
            for (Player player: players) {
                ScoreCard sc = new ScoreCard(this, predictedScoreManager, enteredScoreManager);
                sc.setLayoutParams(params);
                grid.addView(sc);
            }
        }
    }

    class ScoreManager {
        ArrayList<TextView> textViews = new ArrayList<>();

        void addTextView(TextView view) {
            textViews.add(view);
        }

        void enterScores(int[] scores) {
            for (int i = 0; i < scores.length; i++) {
                textViews.get(i).setText(Integer.toString(scores[i]));
            }
        }
    }
}
