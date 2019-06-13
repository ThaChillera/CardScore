package com.robinkuiper.cardsscorekeeper.boerenBridge;

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

import com.robinkuiper.cardsscorekeeper.R;
import com.robinkuiper.cardsscorekeeper.data.PlayerManager;

public class RoundCount extends RelativeLayout {
    final private String TAG = "RoundCount";
    final private Context CONTEXT;
    final private PlayerManager playerManager = PlayerManager.getInstance();
    final private int[] selectedPlayers = playerManager.getSelectedPlayers();

    final int STARTINGPLAYER;

    public RoundCount(Context CONTEXT, BoerenBridge.RoundScoreManager predictedRoundScoreManager, BoerenBridge.RoundScoreManager enteredRoundScoreManager, int roundNumber, int cardCount) {
        super(CONTEXT);
        this.CONTEXT = CONTEXT;
        this.STARTINGPLAYER = (roundNumber -1) % selectedPlayers.length;


        LayoutInflater inflater = (LayoutInflater) CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boeren_bridge_roundcount, this);

        TextView roundNumberView = findViewById(R.id.roundcount_boerenbridge_roundnumber);
        roundNumberView.setText(Integer.toString(roundNumber));

        TextView cardCountView = findViewById(R.id.roundcount_boerenbridge_cardcount);
        cardCountView.setText(Integer.toString(cardCount));

        Button predictScore = findViewById(R.id.roundcount_boerenbridge_predictscore);
        Button enterScore = findViewById(R.id.roundcount_boerenbridge_enterscore);

        predictScore.setOnClickListener(new ButtonOnClickListener(predictedRoundScoreManager, predictScore, enterScore));
        enterScore.setOnClickListener(new ButtonOnClickListener(enteredRoundScoreManager, enterScore, null));
    }

    private int getPlayerIndex(int i) {
        if (i >= selectedPlayers.length) {
            return i - selectedPlayers.length;
        } else {
            return i;
        }
    }

    private class ButtonOnClickListener implements OnClickListener {
        BoerenBridge.RoundScoreManager roundScoreManager;
        Button buttonOld, buttonNew;

        ButtonOnClickListener(BoerenBridge.RoundScoreManager roundScoreManager, Button buttonOld, Button buttonNew) {
            this.roundScoreManager = roundScoreManager;
            this.buttonOld = buttonOld;
            this.buttonNew = buttonNew;
        }

        @Override
        public void onClick(View v) {
            final LinearLayout linearLayout = new LinearLayout(CONTEXT);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            AlertDialog.Builder builder = new AlertDialog.Builder(CONTEXT)
                    .setView(linearLayout);

            if (buttonNew != null) {
                builder.setTitle(R.string.predict_score);
            } else {
                builder.setTitle(R.string.enter_score);
            }

            // Add the buttons
            builder.setPositiveButton(R.string.ok, new DialogPositiveOnClickListener(linearLayout, roundScoreManager, buttonOld, buttonNew))
                    .setNegativeButton(R.string.cancel, null);

            final AlertDialog dialog = builder.show();
            //Fix for edittext not being selectable
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            //for each player, add input to layout
            //order is based on who starts this round, player who starts is at the top
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 3);
            LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

            for (int i = STARTINGPLAYER; i < selectedPlayers.length + STARTINGPLAYER; i++) {
                int index = getPlayerIndex(i);

                LinearLayout innerLayout = new LinearLayout(CONTEXT);
                innerLayout.setOrientation(LinearLayout.HORIZONTAL);

                Space space = new Space(CONTEXT);
                space.setLayoutParams(spaceParams);

                innerLayout.addView(getSpace());

                TextView nameTextView = new TextView(CONTEXT);
                nameTextView.setText(playerManager.getPlayerName(selectedPlayers[index]));
                nameTextView.setGravity(Gravity.CENTER);
                nameTextView.setLayoutParams(params);

                innerLayout.addView(nameTextView);
                innerLayout.addView(getSpace());

                EditText input = new EditText(CONTEXT);

                input.setLayoutParams(params);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                input.setOnFocusChangeListener(new InputOnFocusChangeListener(dialog));
                input.setOnEditorActionListener(new InputOnEditorActionListener(i - STARTINGPLAYER == selectedPlayers.length - 1, dialog));

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
        BoerenBridge.RoundScoreManager roundScoreManager;
        Button buttonOld, buttonNew;

        public DialogPositiveOnClickListener(LinearLayout linearLayout, BoerenBridge.RoundScoreManager roundScoreManager, Button buttonOld, Button buttonNew) {
            this.linearLayout = linearLayout;
            this.roundScoreManager = roundScoreManager;
            this.buttonOld = buttonOld;
            this.buttonNew = buttonNew;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // User clicked OK button
            int[] inputs = new int[selectedPlayers.length];

            //starts at STARTINGPLAYER, since input list starts at STARTINGPLAYER
            for (int i = STARTINGPLAYER; i < selectedPlayers.length + STARTINGPLAYER; i++) {
                int index = getPlayerIndex(i);

                EditText editText = (EditText)((LinearLayout) linearLayout.getChildAt(i - STARTINGPLAYER)).getChildAt(3);

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

            //return values
            roundScoreManager.enterScores(inputs);

            //change buttons
            buttonOld.setVisibility(GONE);
            if (buttonNew != null)
                buttonNew.setVisibility(VISIBLE);
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
