package com.ganeshgfx.quotes

import android.content.Intent
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.allOf

import org.junit.Rule
import org.junit.Test


class MainActivityTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()


    @Test fun testClearButton_expectedDialogBoxOpen() {
        runBlocking {
            delay(2000)
        }
        onView(withId(R.id.clear_history)).perform(click())
        runBlocking {
            delay(2000)
        }
        onView(withText("Clear Quote History..?")).check(matches(isDisplayed()))
    }

    @Test fun testEmptyRecyclerView_expectNoDialogBox(){
        runBlocking {
            delay(2000)
        }
        onView(withId(R.id.clear_history)).perform(click())
        runBlocking {
            delay(2000)
        }
        onView(withText("Clear Quote History..?")).check(doesNotExist())
    }

    @Test fun testRecyclerView_expectEmptyList(){
        runBlocking {
            delay(2000)
        }
        onView(withId(R.id.clear_history)).perform(click())
        runBlocking {
            delay(1000)
        }
        onView(withText("Clear Quote History..?")).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())
        runBlocking {
            delay(500)
        }
        onView(withText("Random Quotes")).check(matches(isDisplayed()))
        runBlocking {
            delay(500)
        }
        onView(withId(R.id.quoteRecyclerview)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test fun testShareButton_expectedIntentChooser() {

        init()
        val expected = allOf(
            hasAction(Intent.ACTION_CHOOSER),
        )
        onView(withId(R.id.shareQuote)).perform(click())
        intended(expected)
        release()
    }

    @Test fun testSettingButton_expectSettingsPage(){
        init()
        val expected = allOf(
            hasComponent(SettingsActivity::class.java.name),
        )
        onView(withId(R.id.open_settings)).perform(click())
        intended(expected)
        release()
    }
}