package com.kitkat.showtime.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kitkat.showtime.R
import com.kitkat.showtime.adapters.ShowAdapter
import com.kitkat.showtime.db.DatabaseHandler
import com.kitkat.showtime.model.ShowModel
import com.kitkat.showtime.network.RetrofitClient
import com.kitkat.showtime.utilities.CheckNetworkStatus
import com.kitkat.showtime.utilities.Constants
import kotlinx.android.synthetic.main.fragment_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MoviesFragment : Fragment() {

    var dbHandler: DatabaseHandler? = null

    lateinit var rvArrivingToday : RecyclerView
    lateinit var rvPopularMovies : RecyclerView
    lateinit var rvTrendingDaily : RecyclerView
    lateinit var rvTrendingWeekly : RecyclerView

    private var arrayList_at = ArrayList<ShowModel.Result>()
    private var arrayList_pm = ArrayList<ShowModel.Result>()
    private var arrayList_td = ArrayList<ShowModel.Result>()
    private var arrayList_tw = ArrayList<ShowModel.Result>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =  inflater.inflate(R.layout.fragment_movies, container, false)

        initViews(view)

        moviesArrivingToday()
        moviesPopular()
        moviesTrendingDaily()
        moviesTrendingWeekly()

        return view
    }

    private fun initViews(view : View) {

        dbHandler = DatabaseHandler(requireContext())

        rvArrivingToday = view.findViewById(R.id.rvArrivingToday)
        rvArrivingToday.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))

        rvPopularMovies = view.findViewById(R.id.rvPopularMovies)
        rvPopularMovies.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))

        rvTrendingDaily = view.findViewById(R.id.rvTrendingDaily)
        rvTrendingDaily.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))

        rvTrendingWeekly = view.findViewById(R.id.rvTrendingWeekly)
        rvTrendingWeekly.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))

    }

    private fun moviesArrivingToday() {

        val type_at = resources.getString(R.string.movies_arriving_today)

        if(CheckNetworkStatus.isOnline(requireContext())) {

            (RetrofitClient.getClient.moviesArrivingToday(resources.getString(R.string.api_key), Constants.PAGE_NUMBER).enqueue(object :
                Callback<ShowModel> {
                @RequiresApi(api = 16)
                override fun onResponse(call: Call<ShowModel>, response: Response<ShowModel>) {

                    when {
                        response.code() == 401 -> {
                            rlArrivingToday.visibility = View.GONE
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 404 -> {
                            rlArrivingToday.visibility = View.GONE
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 200 -> {

                            (dbHandler as DatabaseHandler).deleteShows(type_at)
                            arrayList_at.clear()

                            val size: Int = response.body()?.getResults()!!.size

                            if(size == 0) {
                                rlArrivingToday.visibility = View.GONE
                            }
                            else {
                                for (i in 0 until size) {

                                    val showModel = ShowModel.Result()

                                    showModel.id = response.body()?.getResults()?.get(i)?.id
                                    showModel.backdrop_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.backdrop_path

                                    arrayList_at.add(showModel)
                                    dbHandler?.addTask(showModel, type_at) as Boolean

                                }

                                rvArrivingToday.adapter = ShowAdapter(arrayList_at, requireContext())
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<ShowModel>, th: Throwable) {

                    Toast.makeText(requireContext(), th.message, Toast.LENGTH_SHORT).show()

                }
            }))

        }
        else {

            arrayList_at = (dbHandler as DatabaseHandler).show(type_at)
            rvArrivingToday.adapter = ShowAdapter(arrayList_at, requireContext())

        }

    }

    private fun moviesPopular() {

        val type_mp = resources.getString(R.string.movies_popular)

        if(CheckNetworkStatus.isOnline(requireContext())) {

            (RetrofitClient.getClient.moviesPopular(resources.getString(R.string.api_key), Constants.PAGE_NUMBER).enqueue(object :
                Callback<ShowModel> {
                @RequiresApi(api = 16)
                override fun onResponse(call: Call<ShowModel>, response: Response<ShowModel>) {

                    when {
                        response.code() == 401 -> {
                            rlPopularMovies.visibility = View.GONE
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 404 -> {
                            rlPopularMovies.visibility = View.GONE
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 200 -> {

                            (dbHandler as DatabaseHandler).deleteShows(type_mp)
                            arrayList_pm.clear()

                            val size: Int = response.body()?.getResults()!!.size

                            if(size == 0) {
                                rlPopularMovies.visibility = View.GONE
                            }
                            else {
                                for (i in 0 until size) {

                                    val showModel = ShowModel.Result()

                                    showModel.id = response.body()?.getResults()?.get(i)?.id
                                    showModel.backdrop_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.backdrop_path

                                    arrayList_pm.add(showModel)
                                    dbHandler?.addTask(showModel, type_mp) as Boolean

                                }

                                rvPopularMovies.adapter = ShowAdapter(arrayList_pm, requireContext())
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<ShowModel>, th: Throwable) {

                    Toast.makeText(requireContext(), th.message, Toast.LENGTH_SHORT).show()

                }
            }))

        }
        else {

            arrayList_pm = (dbHandler as DatabaseHandler).show(type_mp)
            rvPopularMovies.adapter = ShowAdapter(arrayList_pm, requireContext())

        }

    }

    private fun moviesTrendingDaily() {

        val type_td = resources.getString(R.string.movies_trending_daily)

        if(CheckNetworkStatus.isOnline(requireContext())) {

            (RetrofitClient.getClient.moviesTrendingDaily(resources.getString(R.string.api_key)).enqueue(object :
                Callback<ShowModel> {
                @RequiresApi(api = 16)
                override fun onResponse(call: Call<ShowModel>, response: Response<ShowModel>) {

                    when {
                        response.code() == 401 -> {
                            rlTrendingDaily.visibility = View.GONE
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 404 -> {
                            rlTrendingDaily.visibility = View.GONE
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 200 -> {

                            (dbHandler as DatabaseHandler).deleteShows(type_td)
                            arrayList_td.clear()

                            val size: Int = response.body()?.getResults()!!.size

                            if(size == 0) {
                                rlTrendingDaily.visibility = View.GONE
                            }
                            else {
                                for (i in 0 until size) {

                                    val showModel = ShowModel.Result()

                                    showModel.id = response.body()?.getResults()?.get(i)?.id
                                    showModel.backdrop_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.backdrop_path

                                    arrayList_td.add(showModel)
                                    dbHandler?.addTask(showModel, type_td) as Boolean

                                }

                                rvTrendingDaily.adapter = ShowAdapter(arrayList_td, requireContext())
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<ShowModel>, th: Throwable) {

                    Toast.makeText(requireContext(), th.message, Toast.LENGTH_SHORT).show()

                }
            }))

        }
        else {

            arrayList_td = (dbHandler as DatabaseHandler).show(type_td)
            rvTrendingDaily.adapter = ShowAdapter(arrayList_td, requireContext())

        }

    }

    private fun moviesTrendingWeekly() {

        val type_tw = resources.getString(R.string.movies_trending_weekly)

        if(CheckNetworkStatus.isOnline(requireContext())) {

            (RetrofitClient.getClient.moviesTrendingWeekly(resources.getString(R.string.api_key)).enqueue(object :
                Callback<ShowModel> {
                @RequiresApi(api = 16)
                override fun onResponse(call: Call<ShowModel>, response: Response<ShowModel>) {

                    when {
                        response.code() == 401 -> {
                            rlTrendingWeekly.visibility = View.GONE
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 404 -> {
                            rlTrendingWeekly.visibility = View.GONE
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 200 -> {

                            (dbHandler as DatabaseHandler).deleteShows(type_tw)
                            arrayList_tw.clear()

                            val size: Int = response.body()?.getResults()!!.size

                            if(size == 0) {
                                rlTrendingWeekly.visibility = View.GONE
                            }
                            else {
                                for (i in 0 until size) {

                                    val showModel = ShowModel.Result()

                                    showModel.id = response.body()?.getResults()?.get(i)?.id
                                    showModel.backdrop_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.backdrop_path

                                    arrayList_tw.add(showModel)
                                    dbHandler?.addTask(showModel, type_tw) as Boolean

                                }

                                rvTrendingWeekly.adapter = ShowAdapter(arrayList_tw, requireContext())
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<ShowModel>, th: Throwable) {

                    Toast.makeText(requireContext(), th.message, Toast.LENGTH_SHORT).show()

                }
            }))

        }
        else {

            arrayList_tw = (dbHandler as DatabaseHandler).show(type_tw)
            rvTrendingWeekly.adapter = ShowAdapter(arrayList_tw, requireContext())

        }

    }

}