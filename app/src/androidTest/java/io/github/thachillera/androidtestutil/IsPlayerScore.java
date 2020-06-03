package io.github.thachillera.androidtestutil;

import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsPlayerScore extends TypeSafeMatcher<View> {
    private final String playerName;

    private IsPlayerScore(String playerName) {
        this.playerName = playerName;
    }

    @Override
    protected boolean matchesSafely(View item) {
        return false;
    }

    @Override
    public void describeTo(Description description) {

    }

    public static Matcher<View> playerScore(String playerName) {
        return new IsPlayerScore(playerName);
    }
}
