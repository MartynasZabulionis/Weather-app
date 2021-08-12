package lt.marcius.weather

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import lt.marcius.weather.mainactivity.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class CityNameTextBehaviorTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun manipulateCityNameText() {
        onView(withId(R.id.enterButton)).perform(click())


        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.enterCityName)))

        // There should be no internet connection!!!
        onView(withId(R.id.editText))
            .perform(typeText("asfagasdgsdg"), closeSoftKeyboard())

        onView(withId(R.id.enterButton)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.errorOccurred)))
    }
}