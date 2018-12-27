package com.robinkuiper.cardsscorekeeper.boerenBridge;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.robinkuiper.cardsscorekeeper.R;

public class ScoreCard extends LinearLayout {
    final String TAG = "ScoreCard";

    TextView predictedRoundsView, scoredRoundsView;
    Button predictRoundsView, scoreRoundsView;

    public ScoreCard(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boeren_bridge_scorecard, this);

        predictedRoundsView = findViewById(R.id.scorecard_boerenbridge_predictedrounds);
        scoredRoundsView = findViewById(R.id.scorecard_boerenbridge_scoredrounds);

        predictRoundsView = findViewById(R.id.scorecard_boerenbridge_predictrounds_button);
        scoreRoundsView = findViewById(R.id.scorecard_boerenbridge_scorerounds_button);

        predictRoundsView.setOnClickListener(createListener(context, predictedRoundsView, predictRoundsView));
        scoreRoundsView.setOnClickListener(createListener(context, scoredRoundsView, scoreRoundsView));
    }

    private OnClickListener createListener(final Context context, final TextView display, final Button button) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle(R.string.predict_score)
                        .setView(input);

                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        try {
                            String stringSelection = input.getText().toString();

                            //verify input
                            Integer.parseInt(stringSelection);

                            display.setText(stringSelection);
                            display.setVisibility(VISIBLE);

                            button.setVisibility(GONE);

                        } catch (NumberFormatException nfe) {
                            Toast toast = Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                final AlertDialog dialog = builder.show();
                input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    }
                });

                input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        //if enter (done) key is pressed
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
                            return true;
                        }
                        return false;
                    }
                });
            }
        };
    }
}
