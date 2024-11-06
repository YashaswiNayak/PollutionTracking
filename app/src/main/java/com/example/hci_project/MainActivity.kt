package com.example.hci_project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.topToolbar)
        setSupportActionBar(toolbar)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Set up the ViewPager with an adapter
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Link the TabLayout with the ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Inflate the custom tab layout
            val customTabView =
                LayoutInflater.from(tabLayout.context).inflate(R.layout.custom_tab, null)
            val tabTitle = customTabView.findViewById<TextView>(R.id.tabTitle)

            // Set the text based on the position
            tabTitle.text = when (position) {
                0 -> "Home"
                1 -> "About"
                else -> "Contact Us"
            }

            // Set the custom view to the tab
            tab.customView = customTabView
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_tips -> {
                // Navigate to TipsActivity
                val intent = Intent(this, TipsActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_faq -> {
                // Navigate to FaqActivity
                val intent = Intent(this, FaqActivity::class.java)
                startActivity(intent)
                true


            }

            else -> super.onOptionsItemSelected(item)
        }


    }
}
