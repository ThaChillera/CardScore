package io.github.thachillera.cardsscorekeeper.interfaces;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.thachillera.cardsscorekeeper.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class GameSelectActivityTest {

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule<>(GameSelectActivity.class);

    @Test
    public void simpleTest() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }
}