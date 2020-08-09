package com.kitkat.showtime.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.kitkat.showtime.R
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

class ShowDetailsActivity : BaseActivity() {

    var context : Context = this@ShowDetailsActivity
    var id : Int = 0
    lateinit var source : String
    lateinit var tabLayout: TabLayout

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

        tabLayout = findViewById(R.id.tabLayout)
    }

    private fun apiCalls() {

        if(source.equals("movies")) {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.related_movies))

            moviesDetails(id)
        }
        else {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.related_tv))

            tvDetails(id)
        }

        tabLayout.addTab(tabLayout.newTab().setText(R.string.more_details))

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

}