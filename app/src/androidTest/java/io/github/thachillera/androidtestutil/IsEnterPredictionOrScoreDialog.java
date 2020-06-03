package io.github.thachillera.androidtestutil;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsEnterPredictionOrScoreDialog extends TypeSafeMatcher<View> {

    private final String playerName;

    private IsEnterPredictionOrScoreDialog(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public boolean matchesSafely(View view) {
        if (view instanceof EditText && view.getParent() instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) view.getParent();
            return linearLayout.getChildCount() == 5
                    && linearLayout.getChildAt(1) instanceof TextView
                    && ((TextView) linearLayout.getChildAt(1)).getText().equals(playerName);
        } else {
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("This is an Edittext in an enter score dialog");
    }

    public static Matcher<View> enterPredictionOrScoreDialog(String playerName) {
        return new IsEnterPredictionOrScoreDialog(playerName);
    }
}
