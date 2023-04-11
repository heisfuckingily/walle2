package com.example.walle

import com.example.walle.R
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener


class IntroActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var viewPager: ViewPager
    private lateinit var dotsLayout: LinearLayout
    private lateinit var btnNext: Button
    private lateinit var btnSkip: Button
    private lateinit var introAdapter: IntroAdapter
    private lateinit var mDots: Array<TextView>
    private lateinit var prefManager: PrefManager
    private var currentItem: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        print("WORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKINGWORKING WPRKING WORKING")
        prefManager = PrefManager(this)
        if (!prefManager.IsFirstLaunch()) {
            launchMainScreen()
            return
        }

        setContentView(R.layout.activity_intro)
        TransparentStatusBar()
        findViews()
        setClickListener()
        setupViewPager()
        addDotsIndicator(0)
    }

    private fun launchMainScreen() {
        prefManager.SetIsFirstLaunch(false)
        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
        finish()
    }

    private fun TransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(Color.TRANSPARENT)
        }
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    private fun setupViewPager() {
        introAdapter = IntroAdapter(this)
        viewPager.adapter = introAdapter
        viewPager.addOnPageChangeListener(pageChangeListener)
    }

    private fun findViews() {
        viewPager = findViewById<ViewPager>(R.id.view_pager)
        dotsLayout = findViewById<LinearLayout>(R.id.layoutDots)
        btnNext = findViewById<Button>(R.id.btn_next)
        btnSkip = findViewById<Button>(R.id.btn_skip)
    }

    private fun setClickListener() {
        btnSkip.setOnClickListener(this)
        btnNext.setOnClickListener(this)
    }

    fun addDotsIndicator(position: Int) {
        // Adding TetView dynamically
        mDots = arrayOfNulls<TextView>(introAdapter.count) as Array<TextView>

        /* Remove aprvious views when called next time
         if not called then views will keep on adding*/dotsLayout.removeAllViews()

        // Set bullets in each dot text view
        for (i in 0 until mDots.size) {
            mDots[i] = TextView(this)
            mDots[i].setText(Html.fromHtml("•")) // Html code for bullet
            mDots[i].setTextSize(35F)
            mDots[i].setTextColor(resources.getColor(R.color.dot_inactive_color))
            dotsLayout.addView(mDots[i])
        }
        if (mDots.size > 0) {
            // change color of the current selected dot
            mDots[position].setTextColor(resources.getColor(R.color.dot_active_color))
        }
    }

    var pageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            currentItem =
                position // currentItem used to get current position and then increase position on click on next button
            addDotsIndicator(position)

            // change the next button text to "next" / "finish"
            if (position == introAdapter.count - 1) {
                // last page, make it "finish" and make the skip button invisible
                btnNext.text = getString(R.string.finish)
                btnSkip.visibility = View.INVISIBLE
            } else {
                // not last page, set "next" text and make skip button visible
                btnNext.text = getString(R.string.next)
                btnSkip.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_next -> if (currentItem < introAdapter.count - 1) {
                ++currentItem // increase the value by 1
                viewPager.currentItem = currentItem // set the layout at next position
            } else launchMainScreen() // launch main screen on last page
            R.id.btn_skip -> launchMainScreen()
        }
    }
}