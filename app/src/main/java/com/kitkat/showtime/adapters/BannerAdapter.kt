package com.kitkat.showtime.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kitkat.showtime.R
import com.kitkat.showtime.model.ShowModel

class BannerAdapter(
    private val context: Context,
    private val images: List<ShowModel.Result>
) :
    PagerAdapter() {
    private val TAG = BannerAdapter::class.java.simpleName
    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }

    @SuppressLint("CheckResult")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.layout_banner, container, false)
        val image_url = view.findViewById<ImageView>(R.id.image_url)

        val requestOptions = RequestOptions()
        requestOptions.error(R.drawable.error)

        Glide.with(context).load(images.get(position).poster_path).into(image_url);

        val viewPager = container as ViewPager
        viewPager.addView(view, 0)
        return view
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }

}