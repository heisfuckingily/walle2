package com.example.walle

import com.example.walle.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter


class IntroAdapter(context: Context): PagerAdapter() {
    private lateinit var layoutInflater: LayoutInflater
    private var context: Context

    init {
        this.context = context;
    }

    var layouts = intArrayOf(
        R.layout.screen_one,
        R.layout.screen_two,
        R.layout.screen_three,
        R.layout.screen_four,
        R.layout.final_screen,
    )

    override fun getCount(): Int {
        return layouts.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(layouts[position], container, false)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view:View = `object` as View
        container.removeView(view)
    }

}