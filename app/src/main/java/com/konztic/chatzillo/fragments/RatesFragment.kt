package com.konztic.chatzillo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.konztic.chatzillo.R
import com.konztic.chatzillo.adapters.RatesAdapter
import com.konztic.chatzillo.dialogs.RateDialog
import com.konztic.chatzillo.models.Rate
import kotlinx.android.synthetic.main.fragment_rates.view.*

class RatesFragment : Fragment() {

    private lateinit var _view: View

    private lateinit var adapter: RatesAdapter
    private val rates: ArrayList<Rate> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _view = inflater.inflate(R.layout.fragment_rates, container, false)

        setUpRecyclerView()
        setUpFab()

        return _view
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = RatesAdapter(rates)

        _view.recycler_view.setHasFixedSize(true)
        _view.recycler_view.layoutManager = layoutManager
        _view.recycler_view.itemAnimator = DefaultItemAnimator()
        _view.recycler_view.adapter = adapter
    }

    private fun setUpFab() {
        _view.fab_rating.setOnClickListener { RateDialog().show(fragmentManager!!, "") }
    }
}