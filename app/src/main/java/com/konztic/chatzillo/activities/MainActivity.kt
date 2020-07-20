package com.konztic.chatzillo.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.konztic.chatzillo.R
import com.konztic.chatzillo.adapters.PagerAdapter
import com.konztic.chatzillo.common.ToolbarActivity
import com.konztic.chatzillo.fragments.ChatFragment
import com.konztic.chatzillo.fragments.InfoFragment
import com.konztic.chatzillo.fragments.RatesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolbarActivity() {

    private var prevBottomSelected: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarToLoad(toolbar_view as Toolbar)

        setUpViewPager(getPagerAdapter())
        setUpBottomNavigationBar()
    }

    private fun getPagerAdapter(): PagerAdapter {
        val adapter = PagerAdapter(supportFragmentManager)

        adapter.addFragment(InfoFragment())
        adapter.addFragment(RatesFragment())
        adapter.addFragment(ChatFragment())

        return adapter
    }

    private fun setUpViewPager(adapter: PagerAdapter) {
        view_pager.adapter = adapter
        view_pager.offscreenPageLimit = adapter.count
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (prevBottomSelected == null) {
                    bottom_navigation.menu.getItem(0).isChecked = false
                } else {
                    prevBottomSelected!!.isChecked = false
                }

                bottom_navigation.menu.getItem(position).isChecked = true
                prevBottomSelected = bottom_navigation.menu.getItem(position)
            }
        })
    }

    private fun setUpBottomNavigationBar() {
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_info -> {
                    view_pager.currentItem = 0;true
                }
                R.id.bottom_nav_rates -> {
                    view_pager.currentItem = 1;true
                }
                R.id.bottom_nav_chat -> {
                    view_pager.currentItem = 2;true
                }
                else -> false
            }
        }
    }
}