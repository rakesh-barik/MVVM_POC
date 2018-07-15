package io.countryInfo.wiki.presentation;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.countryInfo.wiki.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(JUnit4.class)
@LargeTest
public class MainActivityInstrumentationTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void scrollToItemBelowFold_checkItsText() {
        WifiManager wifi = (WifiManager) mActivityRule.getActivity().getSystemService(Context.WIFI_SERVICE);
        if(wifi != null) wifi.setWifiEnabled(true);

        // First scroll to the position that needs to be matched and click on it.
        onView(withId(R.id.info_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Match the text in an item below the fold and check that it's displayed.
        String itemElementText = mActivityRule.getActivity().getResources().getString(
                R.string.item_element_text);
        onView(withText(itemElementText)).check(matches(isDisplayed()));
    }

    @Test
    public void checkNetworkErrorMessage() {
        WifiManager wifi = (WifiManager) mActivityRule.getActivity().getSystemService(Context.WIFI_SERVICE);
        if(wifi != null) wifi.setWifiEnabled(false);
        onView(withId(R.id.pull_to_refresh)).perform(swipeDown());
        onView(withText(R.string.check_network_connection))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        if(wifi != null) wifi.setWifiEnabled(true);
    }

}
