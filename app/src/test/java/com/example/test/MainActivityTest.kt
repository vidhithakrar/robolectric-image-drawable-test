package com.example.test

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Description
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun testImage() {
        launchActivity<MainActivity>()

        // Below assertion should fail as R.drawable.ic_home_black_24dp is set
        onView(withId(R.id.image)).check(matches(withDrawableRes(R.drawable.ic_android_black_24dp)))
    }

    private fun withDrawableRes(@DrawableRes id: Int) =
        object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has drawable resource: $id")
            }

            // The below code does not work as expected in Robolectric for VectorDrawable,
            // where pixels are being matched

//            override fun matchesSafely(item: ImageView): Boolean {
//                val expectedBitmap = item.context.getDrawable(id)?.toBitmap()
//                val actualBitmap = item.drawable.toBitmap()
//                return actualBitmap.sameAs(expectedBitmap)
//            }

            // As per https://github.com/robolectric/robolectric/issues/6620,
            // The below workaround works on Robolectric
            override fun matchesSafely(item: ImageView): Boolean {
                val drawable = ContextCompat.getDrawable(item.context, id)
                return item.drawable.constantState == drawable?.constantState
            }
        }
}