package io.countryInfo.wiki.presentation;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.countryInfo.wiki.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(JUnit4.class)
public class MainActivityInstrumentationTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void scrollToItemBelowFold_checkItsText() {
        WifiManager wifi = (WifiManager) mActivityRule.getActivity().getSystemService(Context.WIFI_SERVICE);
        if(wifi != null) wifi.setWifiEnabled(true);
        // First scroll to the position that needs to be matched and click on it.
        onView(withId(R.id.info_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(9, click()));

        // Match the text in an item below the fold and check that it's displayed.
        String itemElementText = mActivityRule.getActivity().getResources().getString(
                R.string.item_element_text);
        onView(withText(itemElementText)).check(matches(isDisplayed()));
    }

    @Test
    public void checkNetworkConnectionToast() {
        WifiManager wifi = (WifiManager) mActivityRule.getActivity().getSystemService(Context.WIFI_SERVICE);
        if(wifi != null) wifi.setWifiEnabled(false);
        onView(withId(R.id.pull_to_refresh)).perform(swipeDown());
        onView(withText(R.string.network_error))
                .inRoot(withDecorView(not(is(mActivityRule.
                        getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        if(wifi != null) wifi.setWifiEnabled(true);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}
