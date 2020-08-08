package com.kitkat.showtime.activities

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kitkat.showtime.R
import com.kitkat.showtime.fragments.ComingSoonFragment
import com.kitkat.showtime.fragments.MoviesFragment
import com.kitkat.showtime.fragments.TvFragment
import com.kitkat.showtime.utilities.FragmentUtils

class MainActivity : BaseActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    var active_fragment: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener(
            object : BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.action_movies -> {

                            if (!(active_fragment.equals("movies"))) {
                                FragmentUtils.addFragmentsInBarContainer(
                                    MoviesFragment(),
                                    supportFragmentManager
                                )
                            }
                            active_fragment = "movies"

                        }

                        R.id.action_tv -> {

                            if (!(active_fragment.equals("tv"))) {
                                FragmentUtils.addFragmentsInBarContainer(
                                    TvFragment(),
                                    supportFragmentManager
                                )
                            }
                            active_fragment = "tv"

                        }

                        R.id.action_search -> {

                            if (!(active_fragment.equals("search"))) {
                                FragmentUtils.addFragmentsInBarContainer(
                                    ComingSoonFragment(),
                                    supportFragmentManager
                                )
                            }
                            active_fragment = "search"

                        }

                        R.id.action_my_stuffs -> {

                            if (!(active_fragment.equals("my_stuffs"))) {
                                FragmentUtils.addFragmentsInBarContainer(
                                    ComingSoonFragment(),
                                    supportFragmentManager
                                )
                            }
                            active_fragment = "my_stuffs"

                        }

                    }
                    return true
                }
            })

        FragmentUtils.addFragmentsInBarContainer(MoviesFragment(), supportFragmentManager)
        active_fragment="movies"
    }

    internal var doubleBackToExitPressedOnce = false

    //  Back Pressed on Dashborad
    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show()

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            super.onBackPressed()
            return
        }
    }
}