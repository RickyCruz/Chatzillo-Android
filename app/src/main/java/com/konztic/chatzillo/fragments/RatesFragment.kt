package com.konztic.chatzillo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.konztic.chatzillo.R
import com.konztic.chatzillo.adapters.RatesAdapter
import com.konztic.chatzillo.dialogs.RateDialog
import com.konztic.chatzillo.models.NewRateEvent
import com.konztic.chatzillo.models.Rate
import com.konztic.chatzillo.utilities.RxBus
import com.konztic.chatzillo.utilities.toast
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_rates.view.*
import java.util.EventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RatesFragment : Fragment() {

    private lateinit var _view: View

    private lateinit var adapter: RatesAdapter
    private val rates: ArrayList<Rate> = ArrayList()
    private lateinit var scrollListener: RecyclerView.OnScrollListener

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var ratesDBRef: CollectionReference

    private var ratesSubscription: ListenerRegistration? = null
    private lateinit var rateBusListener: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _view = inflater.inflate(R.layout.fragment_rates, container, false)

        setUpRatesDB()
        setUpCurrentUser()

        setUpRecyclerView()
        setUpFab()

        subscribeToRatings()
        subscribeToNewRatings()

        return _view
    }

    private fun setUpRatesDB() {
        ratesDBRef = store.collection("andy-rates")
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = RatesAdapter(rates)

        _view.recycler_view.setHasFixedSize(true)
        _view.recycler_view.layoutManager = layoutManager
        _view.recycler_view.itemAnimator = DefaultItemAnimator()
        _view.recycler_view.adapter = adapter

        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && _view.fab_rating.isShown) {
                    _view.fab_rating.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    _view.fab_rating.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        }

        _view.recycler_view.addOnScrollListener(scrollListener)
    }

    private fun setUpFab() {
        _view.fab_rating.setOnClickListener { RateDialog().show(fragmentManager!!, "") }
    }

    private fun hasUserRated(rates: ArrayList<Rate>): Boolean {
        var result = false

        rates.forEach {
            if (it.userId == currentUser.uid) {
                result = true
            }
        }

        return result
    }

    private fun removeFABIfRated(rated: Boolean) {
        if (rated) {
            _view.fab_rating.hide()
            _view.recycler_view.removeOnScrollListener(scrollListener)
        }
    }

    private fun saveRate(rate: Rate) {
        val newRating = HashMap<String, Any>()
        newRating["userId"] = rate.userId
        newRating["text"] = rate.text
        newRating["rate"] = rate.rate
        newRating["createdAt"] = rate.createdAt
        newRating["profileImgURL"] = rate.profileImgURL

        ratesDBRef.add(newRating)
            .addOnCompleteListener {
                activity!!.toast("Rating added!")
            }
            .addOnFailureListener {
                activity!!.toast("Rating error, try again!")
            }
    }

    private fun subscribeToRatings() {
        ratesSubscription = ratesDBRef
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                    exception?.let {
                        activity!!.toast("Exception!")
                        return
                    }

                    snapshot?.let {
                        rates.clear()

                        val ratesList = it.toObjects(Rate::class.java)
                        rates.addAll(ratesList)

                        removeFABIfRated(hasUserRated(rates))

                        adapter.notifyDataSetChanged()
                        _view.recycler_view.smoothScrollToPosition(0)
                    }
                }
            })
    }

    private fun subscribeToNewRatings() {
        rateBusListener = RxBus.listen(NewRateEvent::class.java).subscribe {
            saveRate(it.rate)
        }
    }

    override fun onDestroyView() {
        _view.recycler_view.removeOnScrollListener(scrollListener)
        rateBusListener.dispose()
        ratesSubscription?.remove()
        super.onDestroyView()
    }
}