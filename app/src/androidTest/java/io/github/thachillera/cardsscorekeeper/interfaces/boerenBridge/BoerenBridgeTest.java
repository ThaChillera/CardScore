package io.github.thachillera.cardsscorekeeper.interfaces.boerenBridge;

import android.util.Log;
import android.widget.Button;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import io.github.thachillera.cardsscorekeeper.R;
import io.github.thachillera.cardsscorekeeper.data.PersistenceManager;
import io.github.thachillera.cardsscorekeeper.data.players.PlayerManager;
import io.github.thachillera.androidtestutil.IsPlayerScore;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static io.github.thachillera.androidtestutil.IsEnterPredictionOrScoreDialog.enterPredictionOrScoreDialog;
import static org.hamcrest.Matchers.allOf;


public class BoerenBridgeTest {
    private final static String[] playerNames = new String[]{"testPlayer1", "testPlayer2", "testPlayer3"};
    private final int roundsPlayed = 33;
    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule<>(BoerenBridge.class);

    @BeforeClass
    public static void before() {
        PlayerManager playerManager = PlayerManager.getInstance();
        playerManager.addPlayer(playerNames[0], "ts1");
        playerManager.addPlayer(playerNames[1], "ts2");
        playerManager.addPlayer(playerNames[2], "ts3");
        playerManager.selectPlayer(playerManager.getActivePlayerIds()[0]);
        playerManager.selectPlayer(playerManager.getActivePlayerIds()[1]);
        playerManager.selectPlayer(playerManager.getActivePlayerIds()[2]);

        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        persistenceManager.setContext(InstrumentationRegistry.getInstrumentation().getContext());
    }

    @Test
    public void FullPlaythrough() {
        try {
            //play round
            for (int i = 1; i < roundsPlayed / 2 + 1; i++) {
                //enter prediction
                onView(allOf(withText(R.string.predict_score), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(scrollTo()).perform(click());
                onView(enterPredictionOrScoreDialog(playerNames[0])).perform(typeText(Integer.toString(i)));
                onView(enterPredictionOrScoreDialog(playerNames[1])).perform(typeText("0"));
                onView(enterPredictionOrScoreDialog(playerNames[2])).perform(typeText("0"));
                onView(allOf(isAssignableFrom(Button.class), withText(R.string.ok))).perform(click());

                //enter score
                onView(allOf(withText(R.string.enter_score), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
                onView(enterPredictionOrScoreDialog(playerNames[0])).perform(typeText(Integer.toString(i)));
                onView(enterPredictionOrScoreDialog(playerNames[1])).perform(typeText("0"));
                onView(enterPredictionOrScoreDialog(playerNames[2])).perform(typeText("0"));
                onView(allOf(isAssignableFrom(Button.class), withText(R.string.ok))).perform(click());

                //validate score
                int[] expectedScores = io.github.thachillera.testutil.GameScoreManagerUtil(3, i);
                onView(IsPlayerScore.playerScore(playerNames[0])).check(Integer.toString(expectedScores[0]));
            }

            for (int i = roundsPlayed / 2 + 1; i > 0; i--) {
                //enter prediction
                onView(allOf(withText(R.string.predict_score), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(scrollTo()).perform(click());
                onView(enterPredictionOrScoreDialog(playerNames[0])).perform(typeText(Integer.toString(i)));
                onView(enterPredictionOrScoreDialog(playerNames[1])).perform(typeText("0"));
                onView(enterPredictionOrScoreDialog(playerNames[2])).perform(typeText("0"));
                onView(allOf(isAssignableFrom(Button.class), withText(R.string.ok))).perform(click());

                //enter score
                onView(allOf(withText(R.string.enter_score), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
                onView(enterPredictionOrScoreDialog(playerNames[0])).perform(typeText(Integer.toString(i)));
                onView(enterPredictionOrScoreDialog(playerNames[1])).perform(typeText("0"));
                onView(enterPredictionOrScoreDialog(playerNames[2])).perform(typeText("0"));
                onView(allOf(isAssignableFrom(Button.class), withText(R.string.ok))).perform(click());
            }
        } catch (Exception e) {
            Log.e("TAG", "FullPlaythrough: ", e);
        }
    }

}