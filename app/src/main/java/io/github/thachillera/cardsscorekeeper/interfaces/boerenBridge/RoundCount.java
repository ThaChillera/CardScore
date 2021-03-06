package io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import io.github.thachillera.cardsscorekeeper.R;
import io.github.thachillera.cardsscorekeeper.data.PersistenceManager;
import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.GameScoreManager;
import io.github.thachillera.cardsscorekeeper.data.game.boerenBridge.ReadOnlyGameScoreManager;
import io.github.thachillera.cardsscorekeeper.data.players.PlayerManager;
import io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge.headers.HeaderManager;
import io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge.rows.RowManager;

/**
 * Square on the left of the score sheet.
 * Keeps track of rounds & contains predict & enter score buttons.
 */
public class RoundCount extends RelativeLayout {
    final int STARTINGPLAYER;
    final private String TAG = "RoundCount";
    final private Context CONTEXT;
    final private PlayerManager playerManager = PlayerManager.getInstance();
    final private GameScoreManager gameScoreManager;
    final private HeaderManager headerManager;
    final private RowManager rowManager;
    private RoundCount nextRound;

    public RoundCount(Context CONTEXT, GameScoreManager gameScoreManager, HeaderManager headerManager, RowManager rowManager, int roundNumber, int cardCount) {
        super(CONTEXT);
        this.CONTEXT = CONTEXT;
        this.STARTINGPLAYER = (roundNumber - 1) % playerManager.getSelectedPlayerCount();
        this.gameScoreManager = gameScoreManager;
        this.headerManager = headerManager;
        this.rowManager = rowManager;

        LayoutInflater inflater = (LayoutInflater) CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boeren_bridge_roundcount, this);

        TextView roundNumberView = findViewById(R.id.roundcount_boerenbridge_roundnumber);
        roundNumberView.setText(Integer.toString(roundNumber));

        TextView cardCountView = findViewById(R.id.roundcount_boerenbridge_cardcount);
        cardCountView.setText(Integer.toString(cardCount));

        Button predictScore = findViewById(R.id.roundcount_boerenbridge_predictscore);
        Button enterScore = findViewById(R.id.roundcount_boerenbridge_enterscore);

        predictScore.setOnClickListener(new ButtonOnClickListener());
        enterScore.setOnClickListener(new ButtonOnClickListener());
    }

    public void setNextRound(RoundCount nextRound) {
        this.nextRound = nextRound;
    }

    /**
     * Change button visibility.
     * Hides the predict button, and possibly the enter score button.
     * Used when loading a save or undo-ing.
     *
     * @param buttonVisible What the button status should be
     */
    void changeButtonVisibility(ButtonVisible buttonVisible) {
        Button predictScore = findViewById(R.id.roundcount_boerenbridge_predictscore);
        Button enterScore = findViewById(R.id.roundcount_boerenbridge_enterscore);

        switch (buttonVisible) {
            case PREDICT:
                enterScore.setVisibility(GONE);
                predictScore.setVisibility(VISIBLE);
                break;
            case SCORE:
                predictScore.setVisibility(GONE);
                enterScore.setVisibility(VISIBLE);
                break;
            case NONE:
            default:
                predictScore.setVisibility(GONE);
                enterScore.setVisibility(GONE);
                break;
        }
    }

    private int getPlayerIndex(int i) {
        if (i >= playerManager.getSelectedPlayerCount()) {
            return i - playerManager.getSelectedPlayerCount();
        } else {
            return i;
        }
    }

    enum ButtonVisible {
        PREDICT, SCORE, NONE
    }

    private class ButtonOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            final LinearLayout linearLayout = new LinearLayout(CONTEXT);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            AlertDialog.Builder builder = new AlertDialog.Builder(CONTEXT)
                    .setView(linearLayout);

            if (gameScoreManager.getNextEntryType() == ReadOnlyGameScoreManager.EntryType.PREDICTION) {
                builder.setTitle(R.string.predict_score);
            } else {
                builder.setTitle(R.string.enter_score);
            }

            // Add the buttons
            builder.setPositiveButton(R.string.ok, new DialogPositiveOnClickListener(linearLayout))
                    .setNegativeButton(R.string.cancel, null);

            final AlertDialog dialog = builder.show();
            //Fix for edittext not being selectable
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            //for each player, add input to layout
            //order is based on who starts this round, player who starts is at the top
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 3);

            for (int i = STARTINGPLAYER; i < playerManager.getSelectedPlayerCount() + STARTINGPLAYER; i++) {
                int index = getPlayerIndex(i);
                long playerId = playerManager.getSelectedPlayers()[index];

                LinearLayout innerLayout = new LinearLayout(CONTEXT);
                innerLayout.setOrientation(LinearLayout.HORIZONTAL);

                innerLayout.addView(getSpace());

                TextView nameTextView = new TextView(CONTEXT);
                nameTextView.setText(playerManager.getPlayerName(playerId));
                nameTextView.setGravity(Gravity.CENTER);
                nameTextView.setLayoutParams(params);

                innerLayout.addView(nameTextView);
                innerLayout.addView(getSpace());

                EditText input = new EditText(CONTEXT);

                input.setLayoutParams(params);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                input.setOnFocusChangeListener(new InputOnFocusChangeListener(dialog));
                input.setOnEditorActionListener(new InputOnEditorActionListener(i - STARTINGPLAYER == playerManager.getSelectedPlayerCount() - 1, dialog));

                innerLayout.addView(input);
                innerLayout.addView(getSpace());

                linearLayout.addView(innerLayout);
            }
        }

        private Space getSpace() {
            Space space = new Space(CONTEXT);
            space.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

            return space;
        }
    }

    private class DialogPositiveOnClickListener implements DialogInterface.OnClickListener {
        LinearLayout linearLayout;

        public DialogPositiveOnClickListener(LinearLayout linearLayout) {
            this.linearLayout = linearLayout;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // User clicked OK button
            int[] inputs = new int[playerManager.getSelectedPlayerCount()];

            //starts at STARTINGPLAYER, since input list starts at STARTINGPLAYER
            for (int i = STARTINGPLAYER; i < playerManager.getSelectedPlayerCount() + STARTINGPLAYER; i++) {
                int index = getPlayerIndex(i);

                EditText editText = (EditText) ((LinearLayout) linearLayout.getChildAt(i - STARTINGPLAYER)).getChildAt(3);

                //verify input
                int input;
                try {
                    input = Integer.parseInt(editText.getText().toString());
                } catch (NumberFormatException nfe) {
                    Toast toast = Toast.makeText(CONTEXT, "Invalid input", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                inputs[index] = input;
            }

            //validate results
            int totalValue = 0;
            for (int value : inputs) {
                if (value < 0) {
                    Toast toast = Toast.makeText(CONTEXT, CONTEXT.getResources().getString(R.string.invalid_number), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                totalValue += value;
            }
            if (gameScoreManager.getNextEntryType() == ReadOnlyGameScoreManager.EntryType.SCORE && totalValue != gameScoreManager.getCardCount(gameScoreManager.getRound())) {
                Toast toast = Toast.makeText(CONTEXT, CONTEXT.getResources().getString(R.string.invalid_score), Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            //save results
            //todo: save playerID with input field for better code quality
            Map<Long, Integer> inputMap = new HashMap<>();
            for (int i = 0; i < playerManager.getSelectedPlayerCount(); i++) {
                inputMap.put(playerManager.getSelectedPlayers()[i], inputs[i]);
            }

            //if this entry is score, update views && activate next round
            if (gameScoreManager.getNextEntryType() == GameScoreManager.EntryType.SCORE) {
                gameScoreManager.enterScores(inputMap);
                PersistenceManager.getInstance().saveGame(gameScoreManager);

                headerManager.updateScores();
                rowManager.updateScores();
                changeButtonVisibility(ButtonVisible.NONE);
                if (gameScoreManager.getRound() != gameScoreManager.getAmountOfRounds()) {
                    nextRound.changeButtonVisibility(ButtonVisible.PREDICT);
                }
            } else {
                gameScoreManager.enterPredictions(inputMap);
                PersistenceManager.getInstance().saveGame(gameScoreManager);

                rowManager.updatePredictions();
                changeButtonVisibility(ButtonVisible.SCORE);
            }

            //change buttons
            changeButtonVisibility(gameScoreManager.getNextEntryType() == ReadOnlyGameScoreManager.EntryType.SCORE ? ButtonVisible.SCORE : ButtonVisible.NONE);
        }
    }

    private class InputOnFocusChangeListener implements View.OnFocusChangeListener {
        private AlertDialog dialog;

        InputOnFocusChangeListener(AlertDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }
    }

    private class InputOnEditorActionListener implements TextView.OnEditorActionListener {
        private boolean finalInput;
        private AlertDialog dialog;

        InputOnEditorActionListener(boolean finalInput, AlertDialog dialog) {
            this.finalInput = finalInput;
            this.dialog = dialog;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //if enter (done) key is pressed
            if (actionId == EditorInfo.IME_ACTION_DONE && finalInput) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
                return true;
            }
            return false;
        }
    }
}
