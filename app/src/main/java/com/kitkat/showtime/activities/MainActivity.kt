package com.kitkat.showtime.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kitkat.showtime.R
import com.kitkat.showtime.db.DatabaseHandler
import com.kitkat.showtime.fragments.ComingSoonFragment
import com.kitkat.showtime.fragments.MoviesFragment
import com.kitkat.showtime.fragments.TvFragment
import com.kitkat.showtime.model.ShowModel
import com.kitkat.showtime.utilities.CheckNetworkStatus
import com.kitkat.showtime.utilities.FragmentUtils
import java.util.ArrayList

class MainActivity : BaseActivity() {

    private var context : Context = this@MainActivity
    var dbHandler: DatabaseHandler? = null
    lateinit var bottomNavigationView: BottomNavigationView
    private var arrayList = ArrayList<ShowModel.Result>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        dbHandler = DatabaseHandler(context)
        arrayList = (dbHandler as DatabaseHandler).show(resources.getString(R.string.tv_arriving_today))

        if(!(CheckNetworkStatus.isOnline(context))){
            if(arrayList.size == 0){
                val intent = Intent(context, NoInternetActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val moviesFragment: Fragment = MoviesFragment()
        val tvFragment: Fragment = TvFragment()
        val comingSoonFragment: Fragment = ComingSoonFragment()
        val fm: FragmentManager = supportFragmentManager
        var active: Fragment = moviesFragment

        FragmentUtils.addFragmentsInBarContainer(comingSoonFragment, supportFragmentManager, comingSoonFragment)
        FragmentUtils.addFragmentsInBarContainer(tvFragment, supportFragmentManager, tvFragment)
        FragmentUtils.addFragmentsInBarContainer(moviesFragment, supportFragmentManager)


        bottomNavigationView.setOnNavigationItemSelectedListener(
            object : BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.action_movies -> {

                            FragmentUtils.hideShowFragmentsInBarContainer(moviesFragment, supportFragmentManager, active)
                            active = moviesFragment

                        }

                        R.id.action_tv -> {

                            FragmentUtils.hideShowFragmentsInBarContainer(tvFragment, supportFragmentManager, active)
                            active = tvFragment

                        }

                        R.id.action_search -> {

                            FragmentUtils.hideShowFragmentsInBarContainer(comingSoonFragment, supportFragmentManager, active)
                            active = comingSoonFragment

                        }

                        R.id.action_my_stuffs -> {

                            FragmentUtils.hideShowFragmentsInBarContainer(comingSoonFragment, supportFragmentManager, active)
                            active = comingSoonFragment

                        }

                    }
                    return true
                }
            })
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