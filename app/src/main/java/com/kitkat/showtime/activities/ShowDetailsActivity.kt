package com.kitkat.showtime.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.kitkat.showtime.R
import com.kitkat.showtime.adapters.ShowAdapter
import com.kitkat.showtime.model.ShowDetailsModel
import com.kitkat.showtime.model.ShowModel
import com.kitkat.showtime.network.RetrofitClient
import com.kitkat.showtime.utilities.CheckNetworkStatus
import com.kitkat.showtime.utilities.Constants
import kotlinx.android.synthetic.main.activity_show_details.*
import kotlinx.android.synthetic.main.loader.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ShowDetailsActivity : BaseActivity() {

    var context : Context = this@ShowDetailsActivity
    var id : Int = 0
    lateinit var source : String
    lateinit var tabLayout: TabLayout
    lateinit var rvSimilar : RecyclerView
    private var arrayList = ArrayList<ShowModel.Result>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_details)

        val extras = intent.extras
        if (extras != null) {
            id = extras.getInt("id")
            source = extras.getString("source").toString()
        }

        initViews()
        apiCalls()
    }

    private fun initViews() {

        rvSimilar = findViewById(R.id.rvSimilar)
        rvSimilar.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))

        tabLayout = findViewById(R.id.tabLayout)
    }

    private fun apiCalls() {

        if(source.equals("movies")) {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.related_movies))

            moviesDetails(id)
            moviesSimilar()
        }
        else {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.related_tv))

            tvDetails(id)
            tvSimilar()
        }

        tabLayout.addTab(tabLayout.newTab().setText(R.string.more_details))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if(tab.position == 0){
                    related.visibility = View.VISIBLE
                    more_details.visibility = View.GONE
                }
                else{
                    related.visibility = View.GONE
                    more_details.visibility = View.VISIBLE
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })


    }

    private fun moviesDetails(movie_id : Int) {

        if(CheckNetworkStatus.isOnline(this)) {

            progressBar.visibility = View.VISIBLE
            linearLayout.visibility = View.GONE

            (RetrofitClient.getClient.moviesDetails(id, resources.getString(R.string.api_key)).enqueue(object :
                Callback<ShowDetailsModel> {
                @RequiresApi(api = 16)
                override fun onResponse(call: Call<ShowDetailsModel>, response: Response<ShowDetailsModel>) {

                    progressBar.visibility = View.GONE
                    linearLayout.visibility = View.VISIBLE

                    when {
                        response.code() == 401 -> {
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 404 -> {
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 200 -> {

                            Glide.with(context).load(Constants.IMAGE_PATH + response.body()?.backdrop_path).into(iv_poster_path)
                            tv_title.text = response.body()?.title
                            tv_overview.text = response.body()?.overview

                            var genres : String = ""
                            val g_size = response.body()?.getGenres()!!.size
                            for (i in 0 until g_size) {

                                genres += response.body()?.getGenres()?.get(i)?.name
                                if(i < g_size-1) {
                                    genres += ", "
                                }
                            }

                            tv_genres.text = genres

                            var languages : String = ""
                            val s_size = response.body()?.getSpokenLanguages()!!.size
                            for (i in 0 until s_size) {

                                languages += response.body()?.getSpokenLanguages()?.get(i)?.name
                                if(i < s_size-1) {
                                    languages += ", "
                                }
                            }

                            tv_languages.text = languages

                            ll_no_of_episodes.visibility = View.GONE
                            ll_no_of_seasons.visibility = View.GONE

                            tv_release_date.text = response.body()?.release_date.toString()

                        }
                    }

                }

                override fun onFailure(call: Call<ShowDetailsModel>, th: Throwable) {

                    progressBar.visibility = View.GONE
                    linearLayout.visibility = View.VISIBLE

                    Toast.makeText(context, th.message, Toast.LENGTH_SHORT).show()

                }
            }))

        }

    }

    private fun tvDetails(movie_id : Int) {

        if(CheckNetworkStatus.isOnline(this)) {

            progressBar.visibility = View.VISIBLE
            linearLayout.visibility = View.GONE

            (RetrofitClient.getClient.tvDetails(id, resources.getString(R.string.api_key)).enqueue(object :
                Callback<ShowDetailsModel> {
                @RequiresApi(api = 16)
                override fun onResponse(call: Call<ShowDetailsModel>, response: Response<ShowDetailsModel>) {

                    progressBar.visibility = View.GONE
                    linearLayout.visibility = View.VISIBLE

                    when {
                        response.code() == 401 -> {
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 404 -> {
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 200 -> {

                            Glide.with(context).load(Constants.IMAGE_PATH + response.body()?.backdrop_path).into(iv_poster_path)
                            tv_title.text = response.body()?.name
                            tv_overview.text = response.body()?.overview

                            var genres : String = ""
                            val g_size = response.body()?.getGenres()!!.size
                            for (i in 0 until g_size) {

                                genres += response.body()?.getGenres()?.get(i)?.name
                                if(i < g_size-1) {
                                    genres += ", "
                                }
                            }

                            tv_genres.text = genres

                            ll_languages.visibility = View.GONE
                            ll_release_date.visibility = View.GONE

                            tv_no_of_episodes.text = response.body()?.number_of_episodes.toString()
                            tv_no_of_seasons.text = response.body()?.number_of_seasons.toString()

                        }
                    }

                }

                override fun onFailure(call: Call<ShowDetailsModel>, th: Throwable) {

                    progressBar.visibility = View.GONE
                    linearLayout.visibility = View.VISIBLE

                    Toast.makeText(context, th.message, Toast.LENGTH_SHORT).show()

                }
            }))

        }

    }

    private fun moviesSimilar() {

        if(CheckNetworkStatus.isOnline(context)) {

            (RetrofitClient.getClient.moviesSimilar(id, resources.getString(R.string.api_key), Constants.PAGE_NUMBER).enqueue(object :
                Callback<ShowModel> {
                @RequiresApi(api = 16)
                override fun onResponse(call: Call<ShowModel>, response: Response<ShowModel>) {

                    when {
                        response.code() == 401 -> {
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 404 -> {
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 200 -> {

                            arrayList.clear()

                            val size: Int = response.body()?.getResults()!!.size

                            if(size != 0) {

                                for (i in 0 until size) {

                                    val showModel = ShowModel.Result()

                                    showModel.id = response.body()?.getResults()?.get(i)?.id
                                    showModel.poster_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.poster_path
                                    showModel.backdrop_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.backdrop_path

                                    arrayList.add(showModel)

                                }

                                rvSimilar.adapter = ShowAdapter(arrayList, context, source)
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<ShowModel>, th: Throwable) {

                    Toast.makeText(context, th.message, Toast.LENGTH_SHORT).show()

                }
            }))

        }

    }

    private fun tvSimilar() {

        if(CheckNetworkStatus.isOnline(context)) {

            (RetrofitClient.getClient.tvSimilar(id, resources.getString(R.string.api_key), Constants.PAGE_NUMBER).enqueue(object :
                Callback<ShowModel> {
                @RequiresApi(api = 16)
                override fun onResponse(call: Call<ShowModel>, response: Response<ShowModel>) {

                    when {
                        response.code() == 401 -> {
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 404 -> {
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 200 -> {

                            arrayList.clear()

                            val size: Int = response.body()?.getResults()!!.size

                            if(size != 0) {

                                for (i in 0 until size) {

                                    val showModel = ShowModel.Result()

                                    showModel.id = response.body()?.getResults()?.get(i)?.id
                                    showModel.poster_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.poster_path
                                    showModel.backdrop_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.backdrop_path

                                    arrayList.add(showModel)

                                }

                                rvSimilar.adapter = ShowAdapter(arrayList, context, source)
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<ShowModel>, th: Throwable) {

                    Toast.makeText(context, th.message, Toast.LENGTH_SHORT).show()

                }
            }))

        }

    }

}