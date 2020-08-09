package com.kitkat.showtime.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kitkat.showtime.R
import com.kitkat.showtime.adapters.ArrivingAdapter
import com.kitkat.showtime.db.DatabaseHandler
import com.kitkat.showtime.model.ShowModel
import kotlinx.android.synthetic.main.activity_arriving_today.*
import java.util.*


class ArrivingTodayActivity : BaseActivity(), View.OnClickListener {

    private var context : Context = this@ArrivingTodayActivity
    lateinit var source : String
    var dbHandler: DatabaseHandler? = null
    private var arrayList = ArrayList<ShowModel.Result>()
    lateinit var rvArrivingToday : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arriving_today)

        initViews()

        val extras = intent.extras
        if (extras != null) {
            source = extras.getString("source").toString()
        }


        if(source.equals("movies")) {
            arrayList = (dbHandler as DatabaseHandler).show(resources.getString(R.string.movies_arriving_today))
        }
        else {
            arrayList = (dbHandler as DatabaseHandler).show(resources.getString(R.string.tv_arriving_today))
        }

        rvArrivingToday.adapter = ArrivingAdapter(arrayList, context, source)

        tv_total_count.setText(arrayList.size.toString() + " videos")

        back.setOnClickListener(this)

    }

    private fun initViews() {

        dbHandler = DatabaseHandler(context)
        rvArrivingToday = findViewById(R.id.rvArrivingToday)
        rvArrivingToday.setLayoutManager(GridLayoutManager(this, 2))

    }

    override fun onClick(v: View?) {
        when (v!!.id)
        {
            R.id.back->
            {
                finish()
            }
        }
    }
}