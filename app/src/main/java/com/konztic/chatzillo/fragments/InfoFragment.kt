package com.konztic.chatzillo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.konztic.chatzillo.R
import com.konztic.chatzillo.models.TotalMessagesEvent
import com.konztic.chatzillo.utilities.RxBus
import com.konztic.chatzillo.utilities.toast
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_info.view.*
import java.util.EventListener

class InfoFragment : Fragment() {
    private lateinit var _view: View

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference

    private var chatSubscription: ListenerRegistration? = null
    private lateinit var infoBusListener: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _view =  inflater.inflate(R.layout.fragment_info, container, false)

        setUpChatDB()
        setUpCurrentUser()
        setUpCurrentUserInfoUI()

        // Total Messages Firebase Style
        // subscribeToTotalMessagesFirebaseStyle()

        // Total Messages Event Bus + Reactive Style
        subscribeToTotalMessagesEventBusReactiveStyle()

        return _view
    }

    private fun setUpChatDB() {
        chatDBRef = store.collection("andy-chats")
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpCurrentUserInfoUI() {
        _view.text_view_info_email.text = currentUser.email
        _view.text_view_info_name.text = currentUser.displayName?.let { currentUser.displayName } ?: run { getString(R.string.info_no_name) }

        currentUser.photoUrl?.let {
            Glide.with(this).load(currentUser.photoUrl).override(256, 256)
                .circleCrop().into(_view.image_view_info_avatar)
        } ?: run {
            Glide.with(this).load(R.drawable.ic_person).override(128, 128)
                .circleCrop().into(_view.image_view_info_avatar)
        }
    }

    private fun subscribeToTotalMessagesFirebaseStyle() {
        chatSubscription = chatDBRef.addSnapshotListener(object : EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot> {
            override fun onEvent(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                exception?.let {
                    activity!!.toast("Exception!")
                    return
                }

                querySnapshot?.let { _view.text_view_info_total_messages.text = "${it.size()}" }
            }
        })
    }

    private fun subscribeToTotalMessagesEventBusReactiveStyle() {
        infoBusListener = RxBus.listen(TotalMessagesEvent::class.java).subscribe {
            _view.text_view_info_total_messages.text = "${it.total}"
        }
    }

    override fun onDestroyView() {
        infoBusListener.dispose()
        chatSubscription?.remove()
        super.onDestroyView()
    }
}