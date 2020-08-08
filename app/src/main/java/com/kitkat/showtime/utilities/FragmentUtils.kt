package com.kitkat.showtime.utilities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.kitkat.showtime.R

class FragmentUtils {

    companion object{

        lateinit var transaction: FragmentTransaction

        fun addFragmentsInBarContainer(fragment: Fragment, fragmentManager: FragmentManager) {
            transaction = fragmentManager.beginTransaction()
            // transaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.slide_up,R.anim.slide_down);
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack

            transaction.replace(R.id.fragment_container_dashboard, fragment)
            // transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit()
        }

        fun addFragmentsInBarContainerBack(fragment: Fragment, fragmentManager: FragmentManager) {
            transaction = fragmentManager.beginTransaction()
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack

            transaction.replace(R.id.fragment_container_dashboard, fragment)
            transaction.addToBackStack(null)
            // transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit()
        }

    }

}