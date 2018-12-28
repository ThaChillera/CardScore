package com.robinkuiper.cardsscorekeeper.boerenBridge;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
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
import android.widget.TextView;
import android.widget.Toast;

import com.robinkuiper.cardsscorekeeper.R;

public class RoundCount extends RelativeLayout {

    public RoundCount(Context context, BoerenBridge.RoundScoreManager predictedRoundScoreManager, BoerenBridge.RoundScoreManager enteredRoundScoreManager, int playerCount, int roundNumber, int cardCount) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boeren_bridge_roundcount, this);

        TextView roundNumberView = findViewById(R.id.roundcount_boerenbridge_roundnumber);
        roundNumberView.setText(Integer.toString(roundNumber));

        TextView cardCountView = findViewById(R.id.roundcount_boerenbridge_cardcount);
        cardCountView.setText(Integer.toString(cardCount));

        Button predictScore = findViewById(R.id.roundcount_boerenbridge_predictscore);
        Button enterScore = findViewById(R.id.roundcount_boerenbridge_enterscore);

        predictScore.setOnClickListener(new ButtonOnClickListener(context, predictedRoundScoreManager, playerCount, predictScore, enterScore));
        enterScore.setOnClickListener(new ButtonOnClickListener(context, enteredRoundScoreManager, playerCount, enterScore, null));
    }

    private class ButtonOnClickListener implements OnClickListener {
        Context context;
        BoerenBridge.RoundScoreManager roundScoreManager;
        int playerCount;
        Button buttonOld, buttonNew;

        ButtonOnClickListener(Context context, BoerenBridge.RoundScoreManager roundScoreManager, int playerCount, Button buttonOld, Button buttonNew) {
            this.context = context;
            this.roundScoreManager = roundScoreManager;
            this.playerCount = playerCount;
            this.buttonOld = buttonOld;
            this.buttonNew = buttonNew;
        }

        @Override
        public void onClick(View v) {
            final LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setView(linearLayout);

            if (buttonNew != null) {
                builder.setTitle(R.string.predict_score);
            } else {
                builder.setTitle(R.string.enter_score);
            }

            // Add the buttons
            builder.setPositiveButton(R.string.ok, new DialogPositiveOnClickListener(linearLayout, context, roundScoreManager, buttonOld, buttonNew))
                    .setNegativeButton(R.string.cancel, null);

            final AlertDialog dialog = builder.show();
            //Fix for edittext not being selectable
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            //for each player, add input to layout
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 25);

            for (int i = 0; i < playerCount; i++) {
                EditText input = new EditText(context);

                input.setLayoutParams(params);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                input.setOnFocusChangeListener(new InputOnFocusChangeListener(dialog));
                input.setOnEditorActionListener(new InputOnEditorActionListener(i == playerCount -1, dialog));

                linearLayout.addView(input);
            }
        }
    }

    private class DialogPositiveOnClickListener implements DialogInterface.OnClickListener {
        LinearLayout linearLayout;
        Context context;
        BoerenBridge.RoundScoreManager roundScoreManager;
        Button buttonOld, buttonNew;

        public DialogPositiveOnClickListener(LinearLayout linearLayout, Context context, BoerenBridge.RoundScoreManager roundScoreManager, Button buttonOld, Button buttonNew) {
            this.linearLayout = linearLayout;
            this.context = context;
            this.roundScoreManager = roundScoreManager;
            this.buttonOld = buttonOld;
            this.buttonNew = buttonNew;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // User clicked OK button
            int[] inputs = new int[linearLayout.getChildCount()];

            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                EditText editText = (EditText) linearLayout.getChildAt(i);

                //verify input
                int input;
                try {
                    input = Integer.parseInt(editText.getText().toString());
                } catch (NumberFormatException nfe) {
                    Toast toast = Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                inputs[i] = input;
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
