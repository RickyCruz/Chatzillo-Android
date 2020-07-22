package com.konztic.chatzillo.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.konztic.chatzillo.R
import com.konztic.chatzillo.models.Rate
import com.konztic.chatzillo.utilities.inflate
import kotlinx.android.synthetic.main.fragment_rates_item.view.*
import java.text.SimpleDateFormat

class RatesAdapter(private val items: List<Rate>): RecyclerView.Adapter<RatesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.fragment_rates_item))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        fun bind(rate: Rate) = with(itemView) {
            text_view_rate.text = rate.text
            text_view_star.text = rate.rate.toString()
            text_view_calendar.text = SimpleDateFormat("dd MMM, yyyy").format(rate.createdAt)

            if (rate.profileImgURL.isEmpty()) {
                Glide.with(this).load(R.drawable.ic_person).override(100, 100)
                    .circleCrop().into(image_view_profile)
            } else {
                Glide.with(this).load(rate.profileImgURL).override(100, 100)
                    .circleCrop().into(image_view_profile)
            }
        }
    }
}