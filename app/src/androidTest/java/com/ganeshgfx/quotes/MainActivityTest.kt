package com.ganeshgfx.quotes

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import org.hamcrest.Matchers.allOf
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun testClearButton_expectRefresh() {
        onView(withId(R.id.clear_history)).perform(click())
        //onView(withId(android.R.id.button1)).perform(click());
        onView(withText("Clear Quote History..?")).check(matches(isDisplayed()));
    }

    @Test
    fun testShareButton_expectedIntentChooser() {
        init()
        val expected = allOf(hasAction(Intent.ACTION_SEND))
        onView(withId(R.id.shareQuote)).perform(click())
        intended(hasAction(Intent.ACTION_SEND))
        release()

    }
}