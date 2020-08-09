package com.kitkat.showtime.fragments

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.kitkat.showtime.R
import com.kitkat.showtime.activities.ArrivingTodayActivity
import com.kitkat.showtime.adapters.BannerAdapter
import com.kitkat.showtime.adapters.ShowAdapter
import com.kitkat.showtime.db.DatabaseHandler
import com.kitkat.showtime.model.ShowModel
import com.kitkat.showtime.network.RetrofitClient
import com.kitkat.showtime.utilities.CheckNetworkStatus
import com.kitkat.showtime.utilities.Constants
import kotlinx.android.synthetic.main.fragment_tv.*
import kotlinx.android.synthetic.main.fragment_tv.view.*
import kotlinx.android.synthetic.main.loader.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class TvFragment : Fragment(), View.OnClickListener {

    var dbHandler: DatabaseHandler? = null
    lateinit var pager_images: ViewPager
    private var countDownTimer: CountDownTimer? = null
    private var source: String = "tv"

    lateinit var rvArrivingToday : RecyclerView
    lateinit var rvPopularTv : RecyclerView
    lateinit var rvTrendingDaily : RecyclerView
    lateinit var rvTrendingWeekly : RecyclerView

    private var arrayListBanner = ArrayList<ShowModel.Result>()
    private var arrayList_at = ArrayList<ShowModel.Result>()
    private var arrayList_pm = ArrayList<ShowModel.Result>()
    private var arrayList_td = ArrayList<ShowModel.Result>()
    private var arrayList_tw = ArrayList<ShowModel.Result>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_tv, container, false)

        initViews(view)

        apiCalls(view)

        view.rlArrivingToday.setOnClickListener(this)

        view.swipeRefreshLayout.setOnRefreshListener {

            visibilityGone(view)

            countDownTimer?.cancel()

            apiCalls(view)

        }

        return view
    }

    private fun initViews(view : View) {

        dbHandler = DatabaseHandler(requireContext())
        pager_images = view.findViewById(R.id.pager_images)

        rvArrivingToday = view.findViewById(R.id.rvArrivingToday)
        rvArrivingToday.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))

        rvPopularTv = view.findViewById(R.id.rvPopularTv)
        rvPopularTv.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))

        rvTrendingDaily = view.findViewById(R.id.rvTrendingDaily)
        rvTrendingDaily.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))

        rvTrendingWeekly = view.findViewById(R.id.rvTrendingWeekly)
        rvTrendingWeekly.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))

    }

    private fun apiCalls(view : View) {

        tvTopRated(view)
        tvArrivingToday(view)
        tvPopular(view)
        tvTrendingDaily(view)
        tvTrendingWeekly(view)
        startTimeCounter(view)

    }

    private fun visibilityGone(view : View) {

        view.rl_pager.visibility = View.GONE

        view.rlArrivingToday.visibility = View.GONE
        view.rlPopularTv.visibility = View.GONE
        view.rlTrendingDaily.visibility = View.GONE
        view.rlTrendingWeekly.visibility = View.GONE

        view.rvArrivingToday.visibility = View.GONE
        view.rvPopularTv.visibility = View.GONE
        view.rvTrendingDaily.visibility = View.GONE
        view.rvTrendingWeekly.visibility = View.GONE

    }

    private fun tvTopRated(view: View) {

        val typeBanner = resources.getString(R.string.tv_banner)
        view.progressBar.visibility = View.VISIBLE

        if(CheckNetworkStatus.isOnline(requireContext())) {

            (RetrofitClient.getClient.tvTopRated(resources.getString(R.string.api_key), Constants.PAGE_NUMBER).enqueue(object :
                Callback<ShowModel> {
                @RequiresApi(api = 16)
                override fun onResponse(call: Call<ShowModel>, response: Response<ShowModel>) {

                    if(swipeRefreshLayout.isRefreshing){
                        swipeRefreshLayout?.isRefreshing = false
                    }

                    view.progressBar.visibility = View.GONE

                    when {
                        response.code() == 401 -> {
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 404 -> {
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                        response.code() == 200 -> {

                            (dbHandler as DatabaseHandler).deleteShows(typeBanner)
                            arrayListBanner.clear()

                            val size: Int = response.body()?.getResults()!!.size

                            if(size == 0) {
                                rl_pager.visibility = View.GONE
                            }
                            else {
                                rl_pager.visibility = View.VISIBLE
                                for (i in 0 until 8) {

                                    val showModel = ShowModel.Result()

                                    showModel.id = response.body()?.getResults()?.get(i)?.id
                                    showModel.poster_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.poster_path
                                    showModel.backdrop_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.backdrop_path

                                    arrayListBanner.add(showModel)
                                    dbHandler?.addTask(showModel, typeBanner) as Boolean

                                }

                                pager_images.adapter = BannerAdapter(requireContext(), arrayListBanner)
                                dots_indicator.setViewPager(pager_images)
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<ShowModel>, th: Throwable) {

                    if(swipeRefreshLayout.isRefreshing){
                        swipeRefreshLayout?.isRefreshing = false
                    }

                    Toast.makeText(requireContext(), th.message, Toast.LENGTH_SHORT).show()

                }
            }))

        }
        else {

            if(view.swipeRefreshLayout.isRefreshing){
                view.swipeRefreshLayout?.isRefreshing = false
            }

            view.progressBar.visibility = View.GONE

            view.rl_pager.visibility = View.VISIBLE

            arrayListBanner = (dbHandler as DatabaseHandler).show(typeBanner)
            pager_images.adapter = BannerAdapter(requireContext(), arrayListBanner)
            view.dots_indicator.setViewPager(pager_images)

        }

    }

    private fun tvArrivingToday(view: View) {

        val type_at = resources.getString(R.string.tv_arriving_today)

        if(CheckNetworkStatus.isOnline(requireContext())) {

            (RetrofitClient.getClient.tvArrivingToday(resources.getString(R.string.api_key), Constants.PAGE_NUMBER).enqueue(object :
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

                            (dbHandler as DatabaseHandler).deleteShows(type_at)
                            arrayList_at.clear()

                            val size: Int = response.body()?.getResults()!!.size

                            if(size != 0) {

                                rlArrivingToday.visibility = View.VISIBLE
                                rvArrivingToday.visibility = View.VISIBLE
                                for (i in 0 until size) {

                                    val showModel = ShowModel.Result()

                                    showModel.id = response.body()?.getResults()?.get(i)?.id
                                    showModel.poster_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.poster_path
                                    showModel.backdrop_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.backdrop_path

                                    if(!(response.body()?.getResults()?.get(i)?.backdrop_path.isNullOrEmpty())){
                                        arrayList_at.add(showModel)
                                        dbHandler?.addTask(showModel, type_at) as Boolean
                                    }

                                }

                                rvArrivingToday.adapter = ShowAdapter(arrayList_at, requireContext(), source)
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

            view.rlArrivingToday.visibility = View.VISIBLE
            view.rvArrivingToday.visibility = View.VISIBLE

            arrayList_at = (dbHandler as DatabaseHandler).show(type_at)
            view.rvArrivingToday.adapter = ShowAdapter(arrayList_at, requireContext(), source)

        }

    }

    private fun tvPopular(view: View) {

        val type_mp = resources.getString(R.string.tv_popular)

        if(CheckNetworkStatus.isOnline(requireContext())) {

            (RetrofitClient.getClient.tvPopular(resources.getString(R.string.api_key), Constants.PAGE_NUMBER).enqueue(object :
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

                            (dbHandler as DatabaseHandler).deleteShows(type_mp)
                            arrayList_pm.clear()

                            val size: Int = response.body()?.getResults()!!.size

                            if(size != 0) {

                                rlPopularTv.visibility = View.VISIBLE
                                rvPopularTv.visibility = View.VISIBLE

                                for (i in 0 until size) {

                                    val showModel = ShowModel.Result()

                                    showModel.id = response.body()?.getResults()?.get(i)?.id
                                    showModel.poster_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.poster_path
                                    showModel.backdrop_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.backdrop_path

                                    arrayList_pm.add(showModel)
                                    dbHandler?.addTask(showModel, type_mp) as Boolean

                                }

                                rvPopularTv.adapter = ShowAdapter(arrayList_pm, requireContext(), source)
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

            view.rlPopularTv.visibility = View.VISIBLE
            view.rvPopularTv.visibility = View.VISIBLE

            arrayList_pm = (dbHandler as DatabaseHandler).show(type_mp)
            view.rvPopularTv.adapter = ShowAdapter(arrayList_pm, requireContext(), source)

        }

    }

    private fun tvTrendingDaily(view: View) {

        val type_td = resources.getString(R.string.tv_trending_daily)

        if(CheckNetworkStatus.isOnline(requireContext())) {

            (RetrofitClient.getClient.tvTrendingDaily(resources.getString(R.string.api_key)).enqueue(object :
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

                            (dbHandler as DatabaseHandler).deleteShows(type_td)
                            arrayList_td.clear()

                            val size: Int = response.body()?.getResults()!!.size

                            if(size != 0) {

                                rlTrendingDaily.visibility = View.VISIBLE
                                rvTrendingDaily.visibility = View.VISIBLE

                                for (i in 0 until size) {

                                    val showModel = ShowModel.Result()

                                    showModel.id = response.body()?.getResults()?.get(i)?.id
                                    showModel.poster_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.poster_path
                                    showModel.backdrop_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.backdrop_path

                                    arrayList_td.add(showModel)
                                    dbHandler?.addTask(showModel, type_td) as Boolean

                                }

                                rvTrendingDaily.adapter = ShowAdapter(arrayList_td, requireContext(), source)
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

            view.rlTrendingDaily.visibility = View.VISIBLE
            view.rvTrendingDaily.visibility = View.VISIBLE

            arrayList_td = (dbHandler as DatabaseHandler).show(type_td)
            view.rvTrendingDaily.adapter = ShowAdapter(arrayList_td, requireContext(), source)

        }

    }

    private fun tvTrendingWeekly(view: View) {

        val type_tw = resources.getString(R.string.tv_trending_weekly)

        if(CheckNetworkStatus.isOnline(requireContext())) {

            (RetrofitClient.getClient.tvTrendingWeekly(resources.getString(R.string.api_key)).enqueue(object :
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

                            (dbHandler as DatabaseHandler).deleteShows(type_tw)
                            arrayList_tw.clear()

                            val size: Int = response.body()?.getResults()!!.size

                            if(size != 0) {

                                rlTrendingWeekly.visibility = View.VISIBLE
                                rvTrendingWeekly.visibility = View.VISIBLE

                                for (i in 0 until size) {

                                    val showModel = ShowModel.Result()

                                    showModel.id = response.body()?.getResults()?.get(i)?.id
                                    showModel.poster_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.poster_path
                                    showModel.backdrop_path = Constants.IMAGE_PATH + response.body()?.getResults()?.get(i)?.backdrop_path

                                    arrayList_tw.add(showModel)
                                    dbHandler?.addTask(showModel, type_tw) as Boolean

                                }

                                rvTrendingWeekly.adapter = ShowAdapter(arrayList_tw, requireContext(), source)
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

            view.rlTrendingWeekly.visibility = View.VISIBLE
            view.rvTrendingWeekly.visibility = View.VISIBLE

            arrayList_tw = (dbHandler as DatabaseHandler).show(type_tw)
            view.rvTrendingWeekly.adapter = ShowAdapter(arrayList_tw, requireContext(), source)

        }

    }

    fun startTimeCounter(view: View) {
        countDownTimer = object : CountDownTimer(50000, 3500) {
            override fun onTick(millisUntilFinished: Long) {
                Log.e("Timer", "Timer")
                if (pager_images.getCurrentItem() < arrayListBanner.size - 1) {
                    pager_images.setCurrentItem(pager_images.getCurrentItem() + 1)
                } else {
                    pager_images.setCurrentItem(0)
                }
            }
            override fun onFinish() {
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        countDownTimer?.cancel()

    }

    override fun onClick(v: View?) {
        when (v!!.id)
        {
            R.id.rlArrivingToday->
            {
                val intent = Intent(context, ArrivingTodayActivity::class.java)
                intent.putExtra("source", source)
                startActivity(intent)
            }
        }
    }

}